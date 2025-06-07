import React, { useState, useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import authRoute from "../../routes/AuthRoute";
import staffHotelRoute from "../../routes/StaffHotelRoute";
import Navbar from "../public/Navbar";
import userRoute from "../../routes/UserRoute";
import customerRoute from "../../routes/CustomerRoute";
import third_background from "./images/third_background.jpg";

const LoginPage = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [showResetPassword, setShowResetPassword] = useState(false);
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();

  // Hàm xử lý lưu thông tin người dùng sau khi đăng nhập
  const handlePostLogin = async (userData, isOAuth2 = false) => {
    const { userId, role, email: userEmail, token } = userData;

    console.log("handlePostLogin - userData:", userData);
    console.log("handlePostLogin - isOAuth2:", isOAuth2);
    console.log("handlePostLogin - role:", role);

    if (!token || !role || !userId || !userEmail) {
      throw new Error("Thiếu thông tin xác thực từ server");
    }

    sessionStorage.setItem("userId", userId);
    sessionStorage.setItem("userEmail", userEmail);
    sessionStorage.setItem("token", token);

    // Xử lý theo role
    if (role === "STAFF") {
      try {
        const hotelResponse = await staffHotelRoute.getHotelIdByUserId(userId);
        if (hotelResponse?.data) {
          sessionStorage.setItem("hotelId", hotelResponse.data);
        } else {
          console.warn("Không tìm thấy hotelId cho userId:", userId);
        }
      } catch (hotelError) {
        console.error("Lỗi khi lấy hotelId:", hotelError);
      }
    } else if (role === "CUSTOMER") {
      console.log("Đang xử lý role CUSTOMER...");
      console.log("userId để gọi API:", userId);
      console.log("customerRoute object:", customerRoute);
      
      try {
        // Đối với OAuth2, luôn luôn gọi API để lấy customerId
        // Đối với đăng nhập thường, cũng gọi API nếu không có sẵn trong userData
        if (isOAuth2 || !userData.customerId) {
          console.log("Gọi API getCustomerByUserId với userId:", userId);
          
          // Kiểm tra xem hàm API có tồn tại không
          if (typeof customerRoute.getCustomerByUserId !== 'function') {
            console.error("customerRoute.getCustomerByUserId không phải là function!");
            console.error("Available methods:", Object.keys(customerRoute));
            throw new Error("API method không tồn tại");
          }
          
          const customerResponse = await customerRoute.getCustomerByUserId(userId);
          console.log("customerResponse full:", customerResponse);
          console.log("customerResponse.data:", customerResponse?.data);
          
          if (customerResponse?.data) {
            const customerId = customerResponse.data.customerId || customerResponse.data.id;
            console.log("customerId extracted:", customerId);
            
            if (customerId) {
              sessionStorage.setItem("customerId", customerId);
              console.log("Đã lưu customerId vào sessionStorage:", customerId);
              console.log("Kiểm tra lại sessionStorage:", sessionStorage.getItem("customerId"));
            } else {
              console.error("customerId is null/undefined:", customerId);
            }
          } else {
            console.warn("Không tìm thấy data trong response cho userId:", userId);
            console.warn("Full response:", customerResponse);
          }
        } else {
          // Nếu đã có customerId từ response đăng nhập thường
          sessionStorage.setItem("customerId", userData.customerId);
          console.log("Đã lưu customerId từ response:", userData.customerId);
        }
      } catch (customerError) {
        console.error("Lỗi khi lấy customerId:", customerError);
        console.error("Error name:", customerError.name);
        console.error("Error message:", customerError.message);
        console.error("Error stack:", customerError.stack);
        if (customerError.response) {
          console.error("API Response status:", customerError.response.status);
          console.error("API Response data:", customerError.response.data);
          console.error("API Response headers:", customerError.response.headers);
        }
        if (customerError.request) {
          console.error("Request details:", customerError.request);
        }
      }
    }

    // Điều hướng theo role
    switch (role) {
      case "ADMIN":
        navigate("/admin");
        break;
      case "STAFF":
        navigate("/staff");
        break;
      case "CUSTOMER":
        navigate("/");
        break;
      default:
        navigate("/dashboard");
    }
  };

  // Xử lý đăng nhập thông thường
  const handleLogin = async (e) => {
    e.preventDefault();
    setError("");
    setIsLoading(true);

    try {
      const response = await authRoute.login({ email, password });

      if (response.title !== "Login successful" || !response.data) {
        throw new Error(response.message || "Đăng nhập không thành công");
      }

      await handlePostLogin(response.data, false);
    } catch (err) {
      setError(
        err.response?.data?.message || err.message || "Đăng nhập thất bại. Vui lòng kiểm tra lại."
      );
      console.error("Lỗi đăng nhập:", err);
    } finally {
      setIsLoading(false);
    }
  };

  // Xử lý redirect từ OAuth2
  useEffect(() => {
    const handleOAuth2Redirect = async () => {
      const token = searchParams.get("token");
      const role = searchParams.get("role");
      const userId = searchParams.get("userId");
      const userEmail = searchParams.get("email");

      console.log("OAuth2 Redirect params:", { token, role, userId, userEmail });

      if (token && role && userId && userEmail) {
        setIsLoading(true);
        try {
          // Đánh dấu đây là OAuth2 login để hàm handlePostLogin biết cần gọi API lấy thêm thông tin
          await handlePostLogin({ token, role, userId, email: userEmail }, true);
        } catch (err) {
          setError("Lỗi khi xử lý đăng nhập Google: " + err.message);
          console.error("Lỗi OAuth2:", err);
        } finally {
          setIsLoading(false);
        }
      }
    };

    handleOAuth2Redirect();
  }, [searchParams]);

  const handleOAuth2Login = (provider) => {
    window.location.href = `http://localhost:8080/oauth2/authorization/${provider}`;
  };

  const ResetPassword = () => {
    const [resetEmail, setResetEmail] = useState("");
    const [message, setMessage] = useState("");
    const [resetError, setResetError] = useState("");

    const handleSubmit = async (e) => {
      e.preventDefault();
      setMessage("");
      setResetError("");

      try {
        const response = await userRoute.resetPassword(resetEmail);
        setMessage(response.message || "Mật khẩu đã được đặt lại. Vui lòng kiểm tra email của bạn.");
      } catch (err) {
        setResetError(err.response?.data?.message || "Lỗi khi đặt lại mật khẩu. Vui lòng thử lại.");
      }
    };

    return (
      <div className="container mt-5" style={{ maxWidth: "400px" }}>
        <div className="card shadow-sm">
          <div className="card-body">
            <h2 className="card-title text-center mb-4">Đặt lại mật khẩu</h2>
            <form onSubmit={handleSubmit}>
              <div className="mb-3">
                <label htmlFor="emailInput" className="form-label">Email của bạn</label>
                <input
                  type="email"
                  className="form-control"
                  id="emailInput"
                  placeholder="Nhập email..."
                  value={resetEmail}
                  onChange={(e) => setResetEmail(e.target.value)}
                  required
                />
              </div>
              {message && <div className="alert alert-success mt-2">{message}</div>}
              {resetError && <div className="alert alert-danger mt-2">{resetError}</div>}
              <button type="submit" className="btn btn-primary w-100">
                Gửi liên kết
              </button>
            </form>
            <div className="mt-4 text-center">
              <button
                type="button"
                onClick={() => setShowResetPassword(false)}
                className="btn btn-link p-0"
                style={{ fontSize: "0.875rem" }}
              >
                ← Quay lại đăng nhập
              </button>
            </div>
          </div>
        </div>
      </div>
    );
  };

  return (
    <div
      className="d-flex align-items-center justify-content-center min-vh-100"
      style={{
        backgroundImage: `url(${third_background})`,
        backgroundSize: "cover",
        backgroundPosition: "center",
        backdropFilter: "blur(5px)",
      }}
    >
      <div
        className="card p-4 shadow"
        style={{ maxWidth: "400px", width: "100%", backgroundColor: "rgba(255, 255, 255, 0.95)" }}
      >
        <Navbar />
        {showResetPassword ? (
          <ResetPassword />
        ) : (
          <>
            <h2 className="text-center mb-4">Đăng Nhập</h2>
            <form onSubmit={handleLogin}>
              <div className="mb-3">
                <input
                  type="email"
                  className="form-control"
                  placeholder="Email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                  disabled={isLoading}
                />
              </div>
              <div className="mb-3">
                <input
                  type="password"
                  className="form-control"
                  placeholder="Mật khẩu"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                  disabled={isLoading}
                />
                {error && <div className="alert alert-danger mt-2">{error}</div>}
                <button
                  type="button"
                  onClick={() => setShowResetPassword(true)}
                  className="text-muted small float-end mt-1 bg-transparent border-0 p-0"
                >
                  Quên mật khẩu?
                </button>
              </div>
              <div className="mb-3 p-2 rounded text-center">
                <button
                  type="button"
                  onClick={() => handleOAuth2Login("google")}
                  className="d-flex align-items-center justify-content-center gap-2 w-100 mb-2 py-2 px-3 border border-secondary-subtle text-muted rounded shadow-sm"
                  disabled={isLoading}
                  style={{
                    backgroundColor: "#fff",
                    transition: "all 0.3s ease",
                  }}
                  onMouseOver={(e) => {
                    e.currentTarget.style.backgroundColor = "#f5f5f5";
                    e.currentTarget.style.borderColor = "#bbb";
                  }}
                  onMouseOut={(e) => {
                    e.currentTarget.style.backgroundColor = "#fff";
                    e.currentTarget.style.borderColor = "#dee2e6";
                  }}
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    viewBox="0 0 48 48"
                    width="24px"
                    height="24px"
                  >
                    <path
                      fill="#FFC107"
                      d="M43.6 20.5h-1.9V20H24v8h11.3C33.5 33.1 29.2 36 24 36c-6.6 0-12-5.4-12-12s5.4-12 12-12c3 0 5.7 1.1 7.8 2.9l5.9-5.9C33.5 6.5 28.1 4 22 4 11.5 4 3 12.5 3 23s8.5 19 19 19s19-8.5 19-19c0-1.3-.1-2.5-.4-3.7z"
                    />
                    <path
                      fill="#FF3D00"
                      d="M6.3 14.7l6.6 4.8C14.5 16.1 18.9 14 24 14c3 0 5.7 1.1 7.8 2.9l5.9-5.9C33.5 6.5 28.1 4 22 4 14.1 4 7.6 8.9 6.3 14.7z"
                    />
                    <path
                      fill="#4CAF50"
                      d="M24 44c5.2 0 10-2 13.6-5.3l-6.3-5.2c-2 1.5-4.5 2.5-7.3 2.5-5.1 0-9.4-2.9-11.4-7.1l-6.5 5C7.6 39.1 14.1 44 24 44z"
                    />
                    <path
                      fill="#1976D2"
                      d="M43.6 20.5h-1.9V20H24v8h11.3c-1.3 3.6-4.2 6.3-7.6 7.6l6.3 5.2C38.1 38.2 43 31.5 43 23c0-1.3-.1-2.5-.4-3.7z"
                    />
                  </svg>
                  <span className="fw-semibold">Đăng nhập với Google</span>
                </button>
              </div>
              <button
                type="submit"
                className="btn btn-dark w-100 mb-3"
                disabled={isLoading}
              >
                {isLoading ? (
                  <>
                    <span
                      className="spinner-border spinner-border-sm me-2"
                      role="status"
                      aria-hidden="true"
                    ></span>
                    Đang đăng nhập...
                  </>
                ) : (
                  "Đăng Nhập"
                )}
              </button>
              <p className="text-center text-muted small">
                Chưa có tài khoản? <a href="/register">Đăng ký ngay</a>
              </p>
            </form>
          </>
        )}
      </div>
    </div>
  );
};

export default LoginPage;