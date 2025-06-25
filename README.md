# 🏠 Real Estate Management Application

A comprehensive real estate management system with integrated M-Pesa payment processing, built with Spring Boot.

## ✨ Features

- **Property Management**: Complete property and unit management
- **Buyer Management**: Customer profiles and purchase tracking
- **Invoice System**: Automated invoice generation and management
- **M-Pesa Integration**: STK Push payments with real-time status tracking
- **Payment Processing**: Multi-method payment support
- **RESTful API**: Comprehensive REST API with OpenAPI documentation
- **Security**: Spring Security integration with CORS support
- **Monitoring**: Health checks and metrics with Actuator
- **Database**: MySQL database integration with Spring Data JPA

- **Email Notifications**: Email notifications for important events

- **Logging**: Logging with Logback and SLF4J

- **Testing**: Unit and integration testing with JUnit, Mockito, and TestContainers

## 🚀 Quick Start

### Prerequisites

- Java 17+
- Maven 3.6+
- PostgreSQL 12+
- Docker & Docker Compose (optional)

### Development Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/Amarsalim30/real-estate-app-backend.git
   cd real-estate-app-backend
   ```

2. **Run setup script**
   ```bash
   chmod +x scripts/setup-dev.sh
   ./scripts/setup-dev.sh
   ```

3. **Configure M-Pesa credentials**
   
   Update `.env.dev` with your M-Pesa sandbox credentials:
   ```bash
   MPESA_CONSUMER_KEY=your_consumer_key
   MPESA_CONSUMER_SECRET=your_consumer_secret
   MPESA_SHORTCODE=174379
   MPESA_PASSKEY=your_passkey
   MPESA_CALLBACK_URL=https://your-domain.com/api/payments/mpesa/callback
   ```

4. **Start the application**
   ```bash
   source .env.dev
   ./mvnw spring-boot:run
   ```

5. **Access the application**
   - API Documentation: http://localhost:8080/swagger-ui.html
   - Health Check: http://localhost:8080/actuator/health

## 🏗️ Architecture

### Core Components

- **Models**: JPA entities for data persistence
- **Repositories**: Data access layer with Spring Data JPA
- **Services**: Business logic layer with transaction management
- **Controllers**: REST API endpoints with validation
- **DTOs**: Data transfer objects for API communication

### M-Pesa Integration

The application provides comprehensive M-Pesa STK Push integration:

- **STK Push Initiation**: Trigger payment requests to customer phones
- **Callback Handling**: Process M-Pesa payment confirmations
- **Status Que
- **Status Queries**: Real-time payment status checking
- **Payment Processing**: Automatic invoice updates and payment records
- **Error Handling**: Comprehensive error management and retry logic

### Database Schema

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   BuyerProfile  │    │     Invoice     │    │    Payment      │
├─────────────────┤    ├─────────────────┤    ├─────────────────┤
│ id (PK)         │◄──┤ buyer_id (FK)   │◄──┤ invoice_id (FK) │
│ email           │    │ unit_id (FK)    │    │ buyer_id (FK)   │
│ phone           │    │ total_amount    │    │ amount          │
│ address         │    │ status          │    │ status          │
│ national_id     │    │ issued_date     │    │ method          │
│ kra_pin         │    └─────────────────┘    │ payment_date    │
└─────────────────┘                           └─────────────────┘
                                                       │
                                              ┌─────────────────┐
                                              │ PaymentDetail   │
                                              ├─────────────────┤
                                              │ payment_id (FK) │
                                              │ transaction_id  │
                                              │ card_details    │
                                              │ bank_details    │
                                              └─────────────────┘
                                                       │
                                              ┌─────────────────┐
                                              │ MpesaPayment    │
                                              ├─────────────────┤
                                              │ checkout_req_id │
                                              │ phone_number    │
                                              │ mpesa_receipt   │
                                              │ callback_data   │
                                              └─────────────────┘
```

## 🔧 Configuration

### Application Properties

```yaml
# Database Configuration
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/real_estate_db
    username: ${DATABASE_USERNAME}
    password: ${DATABASE_PASSWORD}

# M-Pesa Configuration
mpesa:
  consumer-key: ${MPESA_CONSUMER_KEY}
  consumer-secret: ${MPESA_CONSUMER_SECRET}
  shortcode: ${MPESA_SHORTCODE}
  passkey: ${MPESA_PASSKEY}
  base-url: https://sandbox.safaricom.co.ke  # Use https://api.safaricom.co.ke for production
  callback-url: ${MPESA_CALLBACK_URL}
```

### Environment Variables

| Variable | Description | Required |
|----------|-------------|----------|
| `DATABASE_URL` | PostgreSQL connection URL | Yes |
| `DATABASE_USERNAME` | Database username | Yes |
| `DATABASE_PASSWORD` | Database password | Yes |
| `MPESA_CONSUMER_KEY` | M-Pesa API consumer key | Yes |
| `MPESA_CONSUMER_SECRET` | M-Pesa API consumer secret | Yes |
| `MPESA_SHORTCODE` | M-Pesa business shortcode | Yes |
| `MPESA_PASSKEY` | M-Pesa passkey | Yes |
| `MPESA_CALLBACK_URL` | M-Pesa callback URL | Yes |

## 📡 API Endpoints

### M-Pesa Payments

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/payments/mpesa/stk-push` | Initiate STK Push payment |
| `POST` | `/api/payments/mpesa/callback` | M-Pesa callback webhook |
| `GET` | `/api/payments/mpesa/status/{checkoutRequestId}` | Get payment status |
| `GET` | `/api/payments/mpesa/buyer/{buyerId}` | Get buyer's payment history |
| `GET` | `/api/payments/mpesa/invoice/{invoiceId}/summary` | Get invoice payment summary |

### Example: STK Push Request

```bash
curl -X POST http://localhost:8080/api/payments/mpesa/stk-push \
  -H "Content-Type: application/json" \
  -d '{
    "phoneNumber": "254708374149",
    "amount": 1000.00,
    "invoiceId": 1,
    "accountReference": "INV-001",
    "transactionDesc": "Payment for property unit"
  }'
```

### Example: STK Push Response

```json
{
  "success": true,
  "message": "STK Push initiated successfully",
  "data": {
    "checkoutRequestId": "ws_CO_123456789012_1234567890",
    "merchantRequestId": "12345-67890-12345",
    "responseCode": "0",
    "responseDescription": "Success. Request accepted for processing",
    "customerMessage": "Success. Request accepted for processing"
  },
  "timestamp": "2023-12-01T10:30:00"
}
```

## 🧪 Testing

### Running Tests

```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=MpesaStkServiceTest

# Run tests with coverage
./mvnw test jacoco:report
```

### Test Categories

- **Unit Tests**: Service layer business logic
- **Integration Tests**: Database and external API interactions
- **Controller Tests**: REST endpoint validation
- **Security Tests**: Authentication and authorization

### M-Pesa Testing

For M-Pesa integration testing:

1. **Use Sandbox Environment**: Configure sandbox URLs and credentials
2. **Test Phone Numbers**: Use Safaricom provided test numbers
3. **Mock Responses**: Use WireMock for isolated testing
4. **Callback Testing**: Use ngrok for local callback testing

```bash
# Install ngrok
npm install -g ngrok

# Expose local server
ngrok http 8080

# Update callback URL in configuration
MPESA_CALLBACK_URL=https://your-ngrok-id.ngrok.io/api/payments/mpesa/callback
```

## 🚀 Deployment

### Docker Deployment

```bash
# Build and deploy with Docker Compose
chmod +x scripts/deploy.sh
./scripts/deploy.sh
```

### Manual Deployment

```bash
# Build application
./mvnw clean package -DskipTests

# Run database migrations
./mvnw flyway:migrate

# Start application
java -jar target/real-estate-app-0.0.1-SNAPSHOT.jar
```

### Production Considerations

1. **Database**: Use managed PostgreSQL service (AWS RDS, Google Cloud SQL)
2. **Security**: Enable HTTPS and configure proper CORS policies
3. **Monitoring**: Set up application monitoring (Prometheus, Grafana)
4. **Logging**: Configure centralized logging (ELK Stack)
5. **Backup**: Implement database backup strategies
6. **Scaling**: Use load balancers and multiple application instances

## 🔒 Security

### Authentication & Authorization

- Spring Security integration
- JWT token support (configurable)
- Role-based access control
- CORS configuration for cross-origin requests

### M-Pesa Security

- Secure credential management
- Request signing and validation
- Callback URL verification
- Rate limiting and fraud prevention

### Best Practices

- Environment-based configuration
- Secure password storage
- API rate limiting
- Input validation and sanitization
- SQL injection prevention

## 📊 Monitoring & Observability

### Health Checks

```bash
# Application health
curl http://localhost:8080/actuator/health

# Database connectivity
curl http://localhost:8080/actuator/health/db

# Custom health indicators
curl http://localhost:8080/actuator/health/mpesa
```

### Metrics

- Application metrics via Micrometer
- Prometheus integration
- Custom business metrics
- Payment processing metrics

### Logging

- Structured logging with Logback
- Request/response logging
- M-Pesa transaction logging
- Error tracking and alerting

## 🤝 Contributing

1. **Fork the repository**
2. **Create a feature branch**: `git checkout -b feature/amazing-feature`
3. **Commit changes**: `git commit -m 'Add amazing feature'`
4. **Push to branch**: `git push origin feature/amazing-feature`
5. **Open a Pull Request**

### Development Guidelines

- Follow Spring Boot best practices
- Write comprehensive tests
- Update documentation
- Use conventional commit messages
- Ensure code coverage > 80%

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

### Common Issues

**M-Pesa Integration Issues:**
- Verify sandbox/production URLs
- Check credential configuration
- Validate callback URL accessibility
- Review M-Pesa API documentation

**Database Issues:**
- Ensure PostgreSQL is running
- Verify connection parameters
- Check database permissions
- Run Flyway migrations

**Application Startup Issues:**
- Verify Java 17 installation
- Check port availability
- Review application logs
- Validate environment variables

### Getting Help

- 📧 Email: asabatheif@gmail.com
- 🐛 Issues: [GitHub Issues](https://github.com/Amarsalim30/real-estate-app-backend/issues)
- 📖 Documentation: [Wiki](https://github.com/Amarsalim30/real-estate-app-backend/wiki)

## 🗺️ Roadmap

### Phase 1 (Current)
- ✅ Core real estate management
- ✅ M-Pesa STK Push integration
- ✅ Payment processing
- ✅ API documentation
- ✅ Email notifications

### Phase 2 (Upcoming)
- 🔄 Advanced reporting and analytics
- 🔄 Email Attachments Download
- 🔄 SMS integration
- 🔄 Mobile app API enhancements

### Phase 3 (Future)
- 📋 Multi-tenant support
- 📋 Advanced security features
- 📋 Integration with other payment providers
- 📋 Real-time notifications

---

**Built with ❤️ by [Amar Salim](https://github.com/Amarsalim30)**
```
