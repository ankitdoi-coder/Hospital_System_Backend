# 🏥 Healthcare Legacy Monolith (Archived)

## 📌 Overview
This directory contains the original monolithic Spring Boot implementation of the Smart Healthcare System prior to the Spring Cloud Microservices migration.

It is preserved here for:
1. **Migration History & Case Study:** Showcasing the strangler-fig pattern migration from a single monolithic deployment to distributed microservices.
2. **Feature Reference:** Contains all comprehensive monolithic features including:
   - **Groq LLM (Llama 3.3)** AI Medical Assistant Chatbot
   - **Razorpay Payment Gateway** with HMAC-SHA256 signature verification & billing
   - **Cloudinary** cloud image/profile picture streaming
   - **Email Notifications** via JavaMailSender
   - **Redis-backed OTP & JWT Blacklisting**
   - **Full audit logs & prescriptions**

## ⚙️ Running the Monolith Standalone

```bash
# Navigate to this directory
cd legacy-monolith

# Run with local MySQL and Redis
mvn spring-boot:run
```

Or using Docker Compose:
```bash
docker-compose up --build
```
