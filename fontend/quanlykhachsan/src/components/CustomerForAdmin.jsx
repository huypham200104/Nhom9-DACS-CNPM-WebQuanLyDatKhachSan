import React, { useState, useEffect } from 'react';
import 'antd/dist/reset.css'; // Ant Design CSS
import { Table, Input, Button, DatePicker, Space, message, Card, Row, Col, Typography } from 'antd';
import { SearchOutlined, RedoOutlined } from '@ant-design/icons'; // Icons for better UX
import customerRoute from '../routes/CustomerRoute';

const { Search } = Input;
const { Title } = Typography;

const CustomerForAdmin = () => {
    const [customers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(false);
    const [pagination, setPagination] = useState({
        current: 1,
        pageSize: 10,
        total: 0,
    });
    const [searchParams, setSearchParams] = useState({
        keyword: '',
        bookingId: '',
        checkInDate: null,
        checkOutDate: null,
    });

    const columns = [
        {
            title: 'ID Khách hàng',
            dataIndex: 'customerId',
            key: 'customerId',
            sorter: (a, b) => a.customerId - b.customerId,
            width: '15%',
        },
        {
            title: 'Tên khách hàng',
            dataIndex: 'customerName',
            key: 'customerName',
            sorter: (a, b) => a.customerName.localeCompare(b.customerName),
            width: '30%',
        },
        {
            title: 'Số điện thoại',
            dataIndex: 'phone',
            key: 'phone',
            width: '25%',
        },
        {
            title: 'ID Người dùng',
            dataIndex: 'userId',
            key: 'userId',
            width: '20%',
        },
    ];

    const fetchCustomers = React.useCallback(async (params = {}) => {
        setLoading(true);
        try {
            const { page = 1, size = pagination.pageSize } = params;
            const response = await customerRoute.getAllCustomers(page - 1, size);
            
            setCustomers(response.data);
            setPagination((prev) => ({
                ...prev,
                current: page,
                total: response.totalElements || response.data.length,
            }));
        } catch (error) {
            message.error('Lỗi khi tải danh sách khách hàng');
            console.error(error);
        } finally {
            setLoading(false);
        }
    }, [pagination.pageSize]);

    const formatDate = (date) => {
        if (!date) return null;
        return new Date(date).toISOString().split('T')[0]; // Format to YYYY-MM-DD
    };

    const handleSearch = async () => {
        setLoading(true);
        try {
            let response;
            
            if (searchParams.bookingId) {
                response = await customerRoute.getCustomersByBookingId(searchParams.bookingId);
                setCustomers(Array.isArray(response) ? response : [response]);
                setPagination({
                    ...pagination,
                    current: 1,
                    total: Array.isArray(response) ? response.length : 1,
                });
            } 
            else if (searchParams.checkInDate) {
                response = await customerRoute.getCustomersByCheckInDate(
                    formatDate(searchParams.checkInDate)
                );
                setCustomers(response.data);
                setPagination({
                    ...pagination,
                    current: 1,
                    total: response.data.length,
                });
            }
            else if (searchParams.checkOutDate) {
                response = await customerRoute.getCustomersByCheckOutDate(
                    formatDate(searchParams.checkOutDate)
                );
                setCustomers(response.data);
                setPagination({
                    ...pagination,
                    current: 1,
                    total: response.data.length,
                });
            }
            else if (searchParams.keyword) {
                response = await customerRoute.searchCustomers(
                    searchParams.keyword,
                    pagination.current - 1,
                    pagination.pageSize
                );
                setCustomers(response.data);
                setPagination({
                    ...pagination,
                    current: 1,
                    total: response.totalElements || response.data.length,
                });
            } else {
                await fetchCustomers({ page: 1 });
            }
        } catch (error) {
            message.error('Lỗi khi tìm kiếm khách hàng');
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    const handleTableChange = (newPagination) => {
        setPagination(newPagination);
        fetchCustomers({
            page: newPagination.current,
            size: newPagination.pageSize,
        });
    };

    const handleReset = () => {
        setSearchParams({
            keyword: '',
            bookingId: '',
            checkInDate: null,
            checkOutDate: null,
        });
        fetchCustomers({ page: 1 });
    };

    useEffect(() => {
        fetchCustomers({ page: 1 });
    }, [fetchCustomers]);

    return (
        <>
            <div style={{ padding: '24px', background: '#f5f5f5', minHeight: '100vh' }}>
                <Card
                    title={
                        <Title level={3} style={{ margin: 0, color: '#1d39c4' }}>
                            Quản Lý Khách Hàng
                        </Title>
                    }
                    bordered={false}
                    style={{
                        borderRadius: '8px',
                        boxShadow: '0 4px 12px rgba(0, 0, 0, 0.1)',
                        background: '#fff',
                    }}
                    headStyle={{ borderBottom: '2px solid #f0f0f0' }}
                >
                    <Space direction="vertical" size="large" style={{ width: '100%' }}>
                        {/* Search and Filter Section */}
                        <Row gutter={[16, 16]} align="middle">
                            <Col xs={24} sm={12} md={8}>
                                <Search
                                    placeholder="Tìm theo tên hoặc số điện thoại"
                                    value={searchParams.keyword}
                                    onChange={(e) => setSearchParams({ ...searchParams, keyword: e.target.value })}
                                    onSearch={handleSearch}
                                    enterButton={<Button type="primary" icon={<SearchOutlined />}>Tìm</Button>}
                                    style={{ width: '100%' }}
                                />
                            </Col>
                            <Col xs={24} sm={12} md={8}>
                                <Button
                                    type="default"
                                    icon={<RedoOutlined />}
                                    onClick={handleReset}
                                    style={{
                                        width: '100%',
                                        borderRadius: '6px',
                                        background: '#fff',
                                        borderColor: '#d9d9d9',
                                        color: '#595959',
                                    }}
                                >
                                    Đặt Lại
                                </Button>
                            </Col>
                        </Row>

                        {/* <Row gutter={[16, 16]}>
                            <Col xs={24} sm={12}>
                                <DatePicker
                                    style={{ width: '100%', borderRadius: '6px' }}
                                    placeholder="Chọn ngày check-in"
                                    value={searchParams.checkInDate ? new Date(searchParams.checkInDate) : null}
                                    onChange={(date) => setSearchParams({ 
                                        ...searchParams, 
                                        checkInDate: date ? date.toISOString() : null,
                                        checkOutDate: null 
                                    })}
                                />
                            </Col>
                            <Col xs={24} sm={12}>
                                <DatePicker
                                    style={{ width: '100%', borderRadius: '6px' }}
                                    placeholder="Chọn ngày check-out"
                                    value={searchParams.checkOutDate ? new Date(searchParams.checkOutDate) : null}
                                    onChange={(date) => setSearchParams({ 
                                        ...searchParams, 
                                        checkOutDate: date ? date.toISOString() : null,
                                        checkInDate: null 
                                    })}
                                    disabled={!!searchParams.checkInDate}
                                />
                            </Col>
                        </Row> */}

                        {/* Table Section */}
                        <Table
                            columns={columns}
                            rowKey="customerId"
                            dataSource={customers}
                            pagination={{
                                ...pagination,
                                showSizeChanger: true,
                                pageSizeOptions: ['10', '20', '50'],
                                showTotal: (total) => `Tổng: ${total} khách hàng`,
                            }}
                            loading={loading}
                            onChange={handleTableChange}
                            bordered
                            style={{ borderRadius: '6px', overflow: 'hidden' }}
                            rowClassName={() => 'table-row-hover'}
                        />
                    </Space>
                </Card>
            </div>
            <style>
                {`
                    .table-row-hover:hover {
                        background-color: #f0f5ff !important;
                        transition: background-color 0.3s ease;
                    }
                `}
            </style>
        </>
    );
};

export default CustomerForAdmin;