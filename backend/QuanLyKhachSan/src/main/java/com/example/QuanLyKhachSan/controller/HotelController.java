    package com.example.QuanLyKhachSan.controller;

    import com.example.QuanLyKhachSan.dto.HotelDto;
    import com.example.QuanLyKhachSan.entity.Hotel;
    import com.example.QuanLyKhachSan.response.ApiResponse;
    import com.example.QuanLyKhachSan.service.HotelService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.domain.Page;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.MediaType;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.multipart.MultipartFile;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;

    import java.io.IOException;
    import java.util.List;
    import com.fasterxml.jackson.databind.ObjectMapper;

    @RestController
    @RequestMapping("/hotels")
    public class HotelController {

        @Autowired
        private HotelService hotelService;
        private static final Logger logger = LoggerFactory.getLogger(HotelController.class);

        @Autowired
        private ObjectMapper objectMapper;

        @GetMapping("/{hotelId}")
        public ResponseEntity<ApiResponse<?>> getHotelById(@PathVariable String hotelId) {
            try{
                return ResponseEntity.ok(new ApiResponse<>("Hotel found", hotelService.getHotelById(hotelId)));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("Error fetching hotel", e.getMessage()));
            }
        }

        @GetMapping("/all")
        public ResponseEntity<ApiResponse<?>> getAllHotels(
                @RequestParam(defaultValue = "1") int page,
                @RequestParam(defaultValue = "10") int size) {
            try {
                int adjustedPage = page - 1; // Điều chỉnh để page từ client bắt đầu từ 1
                if (adjustedPage < 0) {
                    adjustedPage = 0; // Đảm bảo không có page âm
                }
                Page<HotelDto> hotelPage = hotelService.getAllHotels(adjustedPage, size);
                return ResponseEntity.ok(new ApiResponse<>("All hotels", hotelPage));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("Error fetching hotels", e.getMessage()));
            }
        }

        @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<ApiResponse<?>> addHotel(
                @RequestPart("hotelDto") String hotelDtoJson,
                @RequestPart(value = "files", required = true) List<MultipartFile> files) {
            try {
                logger.info("Received hotelDtoJson: {}, files: {}", hotelDtoJson, files != null ? files.size() : 0);

                Hotel hotel;
                try {
                    hotel = objectMapper.readValue(hotelDtoJson, Hotel.class);
                } catch (IOException e) {
                    logger.error("Failed to parse hotelDto JSON", e);
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse<>("Failed to parse hotelDto JSON: " + e.getMessage(), e.getClass().getName()));
                }

                logger.info("Parsed Hotel: {}", hotel);
                HotelDto hotelResult = hotelService.createHotel(hotel, files);
                return ResponseEntity.ok(new ApiResponse<>("Hotel added", hotelResult));
            } catch (IllegalArgumentException e) {
                logger.error("Invalid argument: {}", e.getMessage(), e);
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Invalid argument: " + e.getMessage(), e.getClass().getName()));
            } catch (Exception e) {
                logger.error("Error adding hotel: {}", e.getMessage(), e);
                return ResponseEntity.internalServerError()
                        .body(new ApiResponse<>("Error adding hotel: " + e.getMessage(), e.getClass().getName()));
            }
        }

        @PutMapping("/{hotelId}")
        public ResponseEntity<ApiResponse<?>> updateHotel(@PathVariable String hotelId,
                                                          @RequestPart("hotelDto") String hotelDtoJson,
                                                          @RequestPart(value = "files", required = false) List<MultipartFile> files) {
            try {
                logger.info("Received hotelDtoJson: {}, files: {}", hotelDtoJson, files != null ? files.size() : 0);

                Hotel hotel;
                try {
                    hotel = objectMapper.readValue(hotelDtoJson, Hotel.class);
                } catch (IOException e) {
                    logger.error("Failed to parse hotelDto JSON", e);
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse<>("Failed to parse hotelDto JSON: " + e.getMessage(), e.getClass().getName()));
                }

                logger.info("Parsed Hotel: {}", hotel);
                HotelDto hotelResult = hotelService.updateHotel(hotelId, hotel, files);
                return ResponseEntity.ok(new ApiResponse<>("Hotel updated", hotelResult));
            } catch (IllegalArgumentException e) {
                logger.error("Invalid argument: {}", e.getMessage(), e);
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Invalid argument: " + e.getMessage(), e.getClass().getName()));
            } catch (Exception e) {
                logger.error("Error updating hotel: {}", e.getMessage(), e);
                return ResponseEntity.internalServerError()
                        .body(new ApiResponse<>("Error updating hotel: " + e.getMessage(), e.getClass().getName()));
            }
        }

        @DeleteMapping("/{hotelId}")
        public ResponseEntity<ApiResponse<?>> deleteHotel(@PathVariable String hotelId) {
            try {
                hotelService.deleteHotel(hotelId);
                return ResponseEntity.ok(new ApiResponse<>("Hotel deleted", null));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("Error deleting hotel", e.getMessage()));
            }
        }

        @PutMapping("/{hotelId}/approve")
        public ResponseEntity<ApiResponse<?>> approveHotel(@PathVariable String hotelId) {
            try {
                return ResponseEntity.ok(new ApiResponse<>("Hotel approved", hotelService.approveHotel(hotelId)));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("Error approving hotel", e.getMessage()));
            }
        }

        @PutMapping("/{hotelId}/reject")
        public ResponseEntity<ApiResponse<?>> rejectHotel(@PathVariable String hotelId) {
            try {
                return ResponseEntity.ok(new ApiResponse<>("Hotel rejected", hotelService.rejectHotel(hotelId)));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("Error rejecting hotel", e.getMessage()));
            }
        }

        @GetMapping("/name/{hotelName}")
        public ResponseEntity<ApiResponse<?>> getHotelByName(@PathVariable String hotelName) {
            try {
                return ResponseEntity.ok(new ApiResponse<>("Hotel found", hotelService.getHotelByName(hotelName)));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("Error fetching hotel", e.getMessage()));
            }
        }

        @GetMapping("/city/{city}")
        public ResponseEntity<ApiResponse<?>> getHotelByCity(
                @PathVariable String city,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size) {
            try {
                Page<HotelDto> hotelPage = hotelService.getHotelByCity(city, page, size);
                return ResponseEntity.ok(new ApiResponse<>("Hotels found", hotelPage));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("Error fetching hotels", e.getMessage()));
            }
        }

        @GetMapping("/district/{district}")
        public ResponseEntity<ApiResponse<?>> getHotelByDistrict(
                @PathVariable String district,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size) {
            try {
                return ResponseEntity.ok(new ApiResponse<>("Hotel found", hotelService.getHotelByDistrict(district, page, size)));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("Error fetching hotel", e.getMessage()));
            }
        }

        @GetMapping("/ward/{ward}")
        public ResponseEntity<ApiResponse<?>> getHotelByWard(@PathVariable String ward,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
            try {
                return ResponseEntity.ok(new ApiResponse<>("Hotel found", hotelService.getHotelByWard(ward, page, size)));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("Error fetching hotel", e.getMessage()));
            }
        }



        @GetMapping("/street/{street}")
        public ResponseEntity<ApiResponse<?>> getHotelByStreet(@PathVariable String street,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
            try {
                return ResponseEntity.ok(new ApiResponse<>("Hotel found", hotelService.getHotelByStreet(street, page, size)));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("Error fetching hotel", e.getMessage()));
            }
        }

        @GetMapping("/search")
        public ResponseEntity<ApiResponse<?>> searchHotels(
                @RequestParam(required = false) String city,
                @RequestParam(required = false) String district,
                @RequestParam(required = false) String ward,
                @RequestParam(required = false) String street,
                @RequestParam(required = false) String houseNumber) {

            // Thay thế dấu + bằng khoảng trắng nếu cần
            city = city != null ? city.replace("+", " ") : null;
            district = district != null ? district.replace("+", " ") : null;
            ward = ward != null ? ward.replace("+", " ") : null;
            street = street != null ? street.replace("+", " ") : null;
            houseNumber = houseNumber != null ? houseNumber.replace("+", " ") : null;

            try {
                return ResponseEntity.ok(new ApiResponse<>("Search results", hotelService.searchHotels(city, district, ward, street, houseNumber)));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("Error searching hotels", e.getMessage()));
            }
        }
        @GetMapping
        public ResponseEntity<ApiResponse<?>> getHotelByUserId(@RequestParam("user_id") String userId) {
            try {
                return ResponseEntity.ok(new ApiResponse<>("Hotel found", hotelService.getHotelByUserId(userId)));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("Error fetching hotel", e.getMessage()));
            }
        }
        @GetMapping("/names")
        public ResponseEntity<ApiResponse<?>> getAllHotelNames() {
            try {
                return ResponseEntity.ok(new ApiResponse<>("Hotel names", hotelService.getAllHotelNames()));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("Error fetching hotel names", e.getMessage()));
            }
        }

    }