# Gmail Integration Demo

A Spring Boot application that demonstrates Gmail API integration for sending and reading emails with OAuth2 authentication.

## 🚀 Features

- **OAuth2 Authentication** with Google Gmail API
- **Send Emails** programmatically through Gmail
- **Read Recent Emails** from your Gmail inbox
- **Web Interface** for easy interaction
- **RESTful API** endpoints for email operations
- **CORS Support** for frontend integration

## 🛠️ Tech Stack

- **Backend**: Spring Boot 3.5.4, Java 21
- **Frontend**: HTML, CSS, JavaScript
- **Authentication**: Google OAuth2
- **Email API**: Gmail API v1
- **Build Tool**: Gradle (Kotlin DSL)

## 📋 Prerequisites

Before running this application, ensure you have:

1. **Java 21** or higher installed
2. **Google Cloud Console** project with Gmail API enabled
3. **OAuth2 credentials** (credentials.json file)

## ⚙️ Setup Instructions

### 1. Google Cloud Console Setup

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select an existing one
3. Enable the **Gmail API**
4. Go to **Credentials** → **Create Credentials** → **OAuth 2.0 Client IDs**
5. Configure OAuth consent screen
6. Set application type to **Web application**
7. Add authorized redirect URIs:
   - `http://localhost:8080/api/gmail/callback`
8. Download the credentials JSON file

### 2. Project Setup

1. Clone this repository
2. Place your `credentials.json` file in `src/main/resources/`
3. The application will create a `tokens/` directory for storing OAuth tokens

### 3. Build and Run

```bash
# Using Gradle wrapper
./gradlew bootRun

# Or on Windows
gradlew.bat bootRun
```

The application will start on `http://localhost:8080`

## 🔧 Configuration

### Application Properties
```properties
spring.application.name=GmailDemo
gmail.credentials.file=credentials.json
server.port=8080
```

### Important Files (Excluded from Git)
- `src/main/resources/credentials.json` - Google OAuth2 credentials
- `tokens/` - Directory containing stored OAuth tokens

## 📚 API Documentation

### Authentication Endpoints

#### Get Authorization URL
```http
GET /api/gmail/auth
```
**Response:**
```json
{
  "auth_url": "https://accounts.google.com/oauth/authorize?..."
}
```

#### Handle OAuth Callback
```http
GET /api/gmail/callback?code={authorization_code}
```
**Response:** HTML page that posts message to parent window

### Email Operations

#### List Recent Emails
```http
GET /api/gmail/list
```
**Response:**
```json
[
  {
    "id": "message_id",
    "subject": "Email Subject",
    "from": "sender@example.com",
    "date": "Mon, 1 Jan 2024 12:00:00 +0000"
  }
]
```

#### Send Email
```http
POST /api/gmail/send
Content-Type: application/json

{
  "to": "recipient@example.com",
  "subject": "Email Subject",
  "body": "Email content"
}
```
**Response:**
```json
{
  "status": "sent"
}
```

## 🌐 Web Interface

Access the web interface at `http://localhost:8080` to:

1. **Authenticate** with your Google account
2. **Send emails** using the form interface
3. **View recent emails** from your inbox

### Web Interface Features
- OAuth2 authentication flow
- Email composition form
- Recent emails display
- Error handling and success notifications
- Responsive design

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/example/gmailDemo/
│   │   ├── GmailDemoApplication.java      # Main application class
│   │   ├── config/
│   │   │   └── CorsConfig.java            # CORS configuration
│   │   ├── controller/
│   │   │   └── GmailController.java       # REST API endpoints
│   │   ├── model/
│   │   │   └── EmailRequest.java          # Email request DTO
│   │   └── service/
│   │       └── GmailService.java          # Gmail API service
│   └── resources/
│       ├── static/
│       │   └── index.html                 # Web interface
│       ├── application.properties         # App configuration
│       └── credentials.json               # OAuth2 credentials (excluded)
└── test/
    └── java/com/example/gmailDemo/
        └── GmailDemoApplicationTests.java # Test class
```

## 🔒 Security Notes

- **credentials.json** contains sensitive OAuth2 client secrets
- **tokens/** directory stores user authentication tokens
- Both are excluded from version control via `.gitignore`
- CORS is configured to allow all origins (modify for production)

## 🚨 Important Security Considerations

1. **Never commit credentials.json** to version control
2. **Secure your OAuth2 redirect URIs** in production
3. **Implement proper error handling** for production use
4. **Configure CORS** appropriately for your domain
5. **Use HTTPS** in production environments

## 🔧 Dependencies

Key dependencies used in this project:

- `spring-boot-starter-web` - Web framework
- `google-api-services-gmail` - Gmail API client
- `google-auth-library-oauth2-http` - OAuth2 authentication
- `google-api-client` - Google API client library
- `jakarta.mail-api` - Email handling
- `jackson-module-kotlin` - JSON processing

## 🐛 Troubleshooting

### Common Issues

1. **"credentials.json not found"**
   - Ensure credentials.json is in `src/main/resources/`

2. **"Not authenticated" error**
   - Complete OAuth2 flow through `/api/gmail/auth` endpoint

3. **CORS errors**
   - Check CorsConfig.java configuration

4. **Port conflicts**
   - Change server.port in application.properties

### Debug Tips

- Check application logs for detailed error messages
- Verify Google Cloud Console API settings
- Ensure OAuth2 redirect URIs match exactly

## 📄 License

This project is for demonstration purposes. Please ensure compliance with Google's API Terms of Service when using Gmail API.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## 📞 Support

For issues and questions:
1. Check the troubleshooting section
2. Review Google Gmail API documentation
3. Check Spring Boot documentation

---

**Note**: This is a demo application. For production use, implement proper security measures, error handling, and follow Google's API best practices.