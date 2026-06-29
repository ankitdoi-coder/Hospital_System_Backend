# Configuration Guide

## Environment-Specific Configuration

This project uses Spring Boot profiles to manage environment-specific configurations.

### Profiles Available

- **dev**: Development environment (default)
- **prod**: Production environment

### File Structure

```
src/main/resources/
├── application.properties          # Base config (profile defaults only)
├── application-dev.properties      # Development overrides
├── application-prod.properties     # Production overrides
├── application.properties.example  # Reference template
.env.example                        # Dev environment variables template
.env.prod.example                   # Prod environment variables template
```

## Running the Application

### Development Environment (Local)

1. **Copy the environment template:**
   ```bash
   cp .env.example .env
   ```

2. **Fill in the .env file with your actual values:**
   ```env
   DB_PASSWORD=your-password
   JWT_SECRET=your-secret
   OAUTH_CLIENT_ID=your-google-client-id
   OAUTH_CLIENT_SECRET=your-google-client-secret
   MAIL_USERNAME=your-email@gmail.com
   MAIL_PASSWORD=your-app-password
   SPRING_PROFILES_ACTIVE=dev
   ```

3. **Run the application:**
   ```bash
   # Using Maven
   mvn spring-boot:run
   
   # Or with profile explicitly set
   mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
   
   # Or set environment variable
   export SPRING_PROFILES_ACTIVE=dev
   mvn spring-boot:run
   ```

4. **Or run the JAR file:**
   ```bash
   mvn clean package
   java -Dspring.profiles.active=dev -jar target/HealthCare-Backend-*.jar
   ```

### Production Environment (Cloud/Server)

1. **Set environment variables in your deployment platform:**
   - AWS: Use Systems Manager Parameter Store or Environment Variables
   - Azure: Use Azure Key Vault or App Service Configuration
   - Docker: Use `docker run -e ENV_VAR=value`
   - Kubernetes: Use Secrets and ConfigMaps

   Example for Docker:
   ```bash
   docker run -e DB_URL=jdbc:mysql://... \
              -e DB_PASSWORD=... \
              -e JWT_SECRET=... \
              -e OAUTH_CLIENT_ID=... \
              -e SPRING_PROFILES_ACTIVE=prod \
              -p 8080:8080 \
              healthcare-backend:latest
   ```

2. **Environment Variables Required for Production:**
   ```
   DB_URL
   DB_USERNAME
   DB_PASSWORD
   JWT_SECRET
   OAUTH_CLIENT_ID
   OAUTH_CLIENT_SECRET
   OAUTH_REDIRECT_URI
   OAUTH_CALLBACK_URI
   CORS_ALLOWED_ORIGINS
   MAIL_USERNAME
   MAIL_PASSWORD
   FILE_UPLOAD_DIR
   SPRING_PROFILES_ACTIVE=prod
   ```

## Security Guidelines

### Secrets Management

✅ **DO:**
- Store secrets in environment variables
- Use `.gitignore` to prevent committing `.env` files
- Use managed secret services (AWS Secrets Manager, Azure Key Vault)
- Rotate credentials regularly
- Use strong, randomly generated secrets (especially for JWT)

❌ **DON'T:**
- Commit secrets to version control
- Hardcode sensitive data in code
- Share secrets in chat or email
- Use weak passwords or default values in production

### .gitignore Configuration

The `.gitignore` file ignores:
- `.env` (all local environment files)
- `.env.local`, `.env.*.local`
- `application-local.properties`
- Any `.pem`, `.key`, `.p12`, `.jks` files

These are examples only; the actual `.*.example` files are committed for reference.

## Configuration Precedence

Spring Boot applies configurations in this order (highest to lowest priority):

1. Environment Variables
2. System Properties (`-Dspring.property=value`)
3. `application-{profile}.properties`
4. `application.properties`
5. Default values in code

## Example: Setting Up for Development

```bash
# Clone repository
git clone <repo-url>
cd HealthCare-Backend

# Create environment file
cp .env.example .env

# Edit .env with your values
# (Use your actual Google OAuth credentials, email password, etc.)

# Run the application
mvn clean install
mvn spring-boot:run
```

The application will:
- Use `application-dev.properties` (dev profile is default)
- Load secrets from `.env` file
- Use verbose logging
- Allow CORS from localhost:5173
- Enable SQL and Hibernate debugging

## Example: Docker Production Deployment

```dockerfile
FROM openjdk:21-jdk-slim

COPY target/HealthCare-Backend-*.jar app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
```

```bash
docker run \
  -e DB_URL="jdbc:mysql://db-host:3306/healthcaredb" \
  -e DB_USERNAME="produser" \
  -e DB_PASSWORD="<secret>" \
  -e JWT_SECRET="<secret>" \
  -e OAUTH_CLIENT_ID="<secret>" \
  -e OAUTH_CLIENT_SECRET="<secret>" \
  -e MAIL_USERNAME="<secret>" \
  -e MAIL_PASSWORD="<secret>" \
  -p 8080:8080 \
  healthcare-backend:latest
```

## Troubleshooting

### Issue: "Could not resolve placeholder"

This means a required environment variable is missing. Check:
1. Is the `.env` file created and contains all required variables?
2. Are the variables exported? (`export VAR_NAME=value`)
3. Check application logs for which variable is missing

### Issue: Using wrong profile settings

Set profile explicitly:
```bash
export SPRING_PROFILES_ACTIVE=dev
# or
java -Dspring.profiles.active=prod -jar app.jar
```

### Issue: Secrets exposed in git

1. Run: `git reset --soft HEAD~1`
2. Fix the files (remove secrets)
3. Re-commit: `git commit -m "Remove secrets"`
4. Force push (if already pushed): `git push --force-with-lease`
