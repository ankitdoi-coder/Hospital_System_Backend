# Deployment Readiness Checklist

## ✅ Current Status: **MOSTLY DEPLOYMENT READY**

Your application is structured well for deployment. Here's the complete checklist:

---

## 🚀 ENVIRONMENT VARIABLES REQUIRED FOR DEPLOYMENT

### **MANDATORY Variables (Must be set):**

| Variable | Purpose | Example |
|----------|---------|---------|
| `SPRING_PROFILES_ACTIVE` | Activate prod profile | `prod` |
| `DB_URL` | Database connection | `jdbc:mysql://db-host:3306/healthcaredb` |
| `DB_USERNAME` | Database user | `prod_user` |
| `DB_PASSWORD` | Database password | `strong-password-here` |
| `JWT_SECRET` | JWT signing key | `min-32-char-random-secure-string` |
| `OAUTH_CLIENT_ID` | Google OAuth ID | From Google Cloud Console |
| `OAUTH_CLIENT_SECRET` | Google OAuth secret | From Google Cloud Console |
| `MAIL_USERNAME` | Email sender | `noreply@yourdomain.com` |
| `MAIL_PASSWORD` | Email password/token | App-specific password |

### **OPTIONAL Variables (Defaults provided):**

| Variable | Purpose | Default |
|----------|---------|---------|
| `OAUTH_REDIRECT_URI` | Frontend OAuth callback | `https://yourdomain.com/oauth2/redirect` |
| `OAUTH_CALLBACK_URI` | Backend OAuth callback | `https://api.yourdomain.com/api/auth/oauth2/callback` |
| `CORS_ALLOWED_ORIGINS` | Allowed frontend origins | `https://yourdomain.com` |
| `FILE_UPLOAD_DIR` | Upload directory path | `/var/www/uploads/profile-pictures/` |
| `MAIL_HOST` | SMTP server | `smtp.gmail.com` |
| `MAIL_PORT` | SMTP port | `587` |

---

## 📋 DEPLOYMENT CHECKLIST

### **Before Deploying:**

- [ ] **Database Setup**
  - [ ] Cloud MySQL database provisioned (AWS RDS, Azure MySQL, etc.)
  - [ ] Database name: `healthcaredb`
  - [ ] User with CREATE, INSERT, UPDATE, SELECT permissions created
  - [ ] Network access configured (security groups, firewall)
  - [ ] Backups enabled

- [ ] **Google OAuth Setup**
  - [ ] OAuth credentials created in Google Cloud Console
  - [ ] Authorized redirect URIs configured for your domain
  - [ ] Client ID and Secret stored securely (not in code)

- [ ] **Email Configuration**
  - [ ] Email service configured (Gmail, SendGrid, AWS SES)
  - [ ] SMTP credentials verified and working
  - [ ] For Gmail: App-specific password created

- [ ] **SSL/HTTPS**
  - [ ] SSL certificate obtained (Let's Encrypt, AWS ACM, etc.)
  - [ ] HTTPS enabled on domain
  - [ ] Mixed content warning checked (all assets use HTTPS)

- [ ] **File Upload Storage**
  - [ ] Upload directory created with proper permissions
  - [ ] OR: Use cloud storage (AWS S3, Azure Blob Storage)
  - [ ] Disk space allocated for profile pictures

- [ ] **Secrets Management**
  - [ ] All secrets stored in secure vault (AWS Secrets Manager, Azure Key Vault)
  - [ ] No hardcoded secrets in code
  - [ ] No secrets in git history

---

## 🐳 DEPLOYMENT OPTIONS

### **Option 1: Docker (Recommended for Cloud)**

```dockerfile
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy built JAR
COPY target/HealthCare-Backend-*.jar app.jar

# Create non-root user
RUN useradd -m -u 1000 appuser && chown appuser:appuser app.jar
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", \
    "-Dspring.profiles.active=prod", \
    "-jar", \
    "app.jar"]
```

**Build and Run:**
```bash
# Build JAR
mvn clean package -DskipTests

# Build Docker image
docker build -t healthcare-backend:1.0.0 .

# Run with environment variables
docker run -d \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_URL="jdbc:mysql://db-host:3306/healthcaredb" \
  -e DB_USERNAME="prod_user" \
  -e DB_PASSWORD="strong-password" \
  -e JWT_SECRET="random-32-char-secret" \
  -e OAUTH_CLIENT_ID="your-client-id" \
  -e OAUTH_CLIENT_SECRET="your-client-secret" \
  -e MAIL_USERNAME="noreply@domain.com" \
  -e MAIL_PASSWORD="app-password" \
  -p 8080:8080 \
  healthcare-backend:1.0.0
```

### **Option 2: Kubernetes**

Create `deployment.yaml`:
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: healthcare-backend
spec:
  replicas: 2
  selector:
    matchLabels:
      app: healthcare-backend
  template:
    metadata:
      labels:
        app: healthcare-backend
    spec:
      containers:
      - name: healthcare-backend
        image: healthcare-backend:1.0.0
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "prod"
        - name: DB_URL
          valueFrom:
            secretKeyRef:
              name: app-secrets
              key: db-url
        - name: DB_USERNAME
          valueFrom:
            secretKeyRef:
              name: app-secrets
              key: db-username
        - name: DB_PASSWORD
          valueFrom:
            secretKeyRef:
              name: app-secrets
              key: db-password
        # ... other env vars from secrets
        livenessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 20
          periodSeconds: 5
```

Create secrets:
```bash
kubectl create secret generic app-secrets \
  --from-literal=db-url="jdbc:mysql://..." \
  --from-literal=db-username="prod_user" \
  --from-literal=db-password="password" \
  --from-literal=jwt-secret="secret" \
  # ... other secrets
```

### **Option 3: Cloud Platforms**

**AWS Elastic Beanstalk:**
```bash
# Create .ebextensions/app.config
option_settings:
  aws:autoscaling:launchconfiguration:
    InstanceType: t3.medium
  aws:elasticbeanstalk:application:environment:
    SPRING_PROFILES_ACTIVE: prod
    DB_URL: jdbc:mysql://rds-endpoint:3306/healthcaredb
    # ... other env vars
```

**Azure App Service:**
```bash
az webapp config appsettings set \
  --resource-group myResourceGroup \
  --name myAppName \
  --settings \
    SPRING_PROFILES_ACTIVE=prod \
    DB_URL="jdbc:mysql://..." \
    # ... other env vars
```

**Heroku:**
```bash
heroku config:set \
  SPRING_PROFILES_ACTIVE=prod \
  DB_URL="jdbc:mysql://..." \
  # ... other env vars
```

---

## ⚠️ PRODUCTION CONSIDERATIONS

### **Not Yet Implemented (Add Later):**

- [ ] **Database Migrations** - Consider Flyway or Liquibase for schema versioning
- [ ] **Rate Limiting** - Protect APIs from abuse
- [ ] **API Documentation** - Swagger/OpenAPI for API reference
- [ ] **Monitoring/Logging** - ELK Stack, DataDog, New Relic, etc.
- [ ] **Error Tracking** - Sentry, Rollbar for exception monitoring
- [ ] **Security Headers** - Add CSRF protection, CSP, etc.
- [ ] **Input Validation** - Strengthen validation across all endpoints
- [ ] **Testing** - Unit, integration, and E2E tests
- [ ] **Performance** - Caching, database optimization

---

## 📝 BUILD FOR PRODUCTION

```bash
# 1. Build the JAR
mvn clean package -DskipTests

# 2. The JAR will be in: target/HealthCare-Backend-*.jar

# 3. You can run it directly:
java -Dspring.profiles.active=prod \
     -Dspring.datasource.url=jdbc:mysql://db:3306/healthcaredb \
     -Dspring.datasource.username=prod_user \
     -Dspring.datasource.password=strong-pass \
     -Dapp.jwt.secret=random-secret \
     -jar target/HealthCare-Backend-*.jar
```

**Or using environment variables:**
```bash
export SPRING_PROFILES_ACTIVE=prod
export DB_URL="jdbc:mysql://db:3306/healthcaredb"
export DB_USERNAME="prod_user"
export DB_PASSWORD="strong-pass"
export JWT_SECRET="random-secret"
export OAUTH_CLIENT_ID="google-id"
export OAUTH_CLIENT_SECRET="google-secret"
export MAIL_USERNAME="email@domain.com"
export MAIL_PASSWORD="app-password"

java -jar target/HealthCare-Backend-*.jar
```

---

## ✅ QUICK DEPLOYMENT SCRIPT

Save as `deploy.sh`:
```bash
#!/bin/bash

# Build
echo "Building application..."
mvn clean package -DskipTests || exit 1

# Docker build
echo "Building Docker image..."
docker build -t healthcare-backend:latest .

# Push to registry (optional)
# docker push your-registry/healthcare-backend:latest

# Run (or deploy to Kubernetes/Cloud)
echo "Deployment ready!"
echo "Run with: docker run -e SPRING_PROFILES_ACTIVE=prod -e DB_URL=... healthcare-backend:latest"
```

---

## 🔒 Security Reminders

✅ **DO:**
- Store all secrets in environment variables or secret manager
- Use HTTPS only in production
- Enable SSL certificate validation
- Restrict CORS to your domain only
- Use strong, randomly generated JWT secret
- Rotate secrets regularly
- Enable database backups
- Monitor application logs

❌ **DON'T:**
- Hardcode secrets in application.properties
- Commit `.env` file to git
- Use default passwords
- Allow CORS from `*`
- Use weak JWT secrets
- Skip HTTPS in production

---

## Summary

**✅ Your app is deployment-ready!** Just ensure you:

1. Set all **MANDATORY** environment variables
2. Use a production database (MySQL/PostgreSQL in cloud)
3. Configure HTTPS/SSL
4. Store secrets securely
5. Build and deploy using Docker or your cloud platform

Need help with any specific deployment platform?
