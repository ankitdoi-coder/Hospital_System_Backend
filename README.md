<div align="center">

# 🏥 Smart Healthcare System — Backend

**A production-ready, secure, and scalable RESTful API** for a **Smart Healthcare Appointment & Records System**, built with **Java 17 + Spring Boot 3.5**.
Implements real-world engineering practices including JWT-based auth, role-based access control, centralized exception handling, request validation, OAuth2 social login, **Razorpay payment gateway integration**, billing management, **Cloudinary-backed cloud file storage**, and automated API documentation.

[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)](https://spring.io/projects/spring-security)
[![MySQL](https://img.shields.io/badge/MySQL-8.3-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Maven](https://img.shields.io/badge/Maven-3.x-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![Swagger](https://img.shields.io/badge/API%20Docs-Swagger%2FOpenAPI-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)](https://swagger.io/)
[![Razorpay](https://img.shields.io/badge/Payments-Razorpay-0C2451?style=for-the-badge&logo=razorpay&logoColor=white)](https://razorpay.com/)
[![Cloudinary](https://img.shields.io/badge/Media%20Storage-Cloudinary-3448C5?style=for-the-badge&logo=cloudinary&logoColor=white)](https://cloudinary.com/)

[![License](https://img.shields.io/badge/License-MIT-yellow.svg?style=flat-square)](#)
[![Build](https://img.shields.io/badge/Build-Passing-brightgreen.svg?style=flat-square)](#)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)](#)
[![Made with Java](https://img.shields.io/badge/Made%20with-Java%20Full%20Stack-orange.svg?style=flat-square)](#)

**Frontend Repository:** [🔗 HealthCare-Frontend](https://github.com/ankitdoi-coder/HealthCare-Frontend) — React 19 | Vite | Redux Toolkit | Tailwind CSS | Razorpay Checkout

</div>

---

## 🎥 Video Walkthroughs (Explained by Me)

I've recorded short walkthroughs breaking down some of the trickier features and bugs in this project — not just showing that it works, but explaining the *reasoning* behind the implementation.

| # | Topic | Link |
|---|---|---|
| 1 | 🔔 Notification Feature — Overview | [▶ Watch](https://youtu.be/mJa_I60yNYk?si=jBbiUTr2HLxPM4bt) |
| 2 | 🧩 Notification Feature — Service & Repository Layer | [▶ Watch](https://youtu.be/Rj3RG-A-wn0?si=nwBGXUSvE3xxUsSR) |
| 3 | ✅ In-App Notification Feature — Completed Walkthrough | [▶ Watch](https://youtu.be/fs9LxIsQvME?si=rK6OaHf02rvm3U3-) |
| 4 | 🐞 JWT Authentication Bug Fix — Root Cause & Resolution | [▶ Watch](https://youtu.be/uyvSxrhkSR8?si=9AJxx--k1zeFRAHy) |
| 5 | ✅ Bean Validation & Global Exception Handler | [▶ Watch](https://youtu.be/j93XCeoUj28?si=NL4z7jZyLpFrPAt6) |
| 6 | 📧 OTP-Based Registration Flow | [▶ Watch](https://youtu.be/8FZdOrmtN2A?si=ZC2RkhXvxhLi4Wuo) |
| 7 | 🔑 OTP Email-Based Password Reset | [▶ Watch](https://youtu.be/1MH0xzRQ0OM?si=_MpSCJ_vM0tZSQea) |

> 💡 These videos are meant to give reviewers a look into my thought process — how I debug, design, and reason through real backend problems, not just the final code.

---

## ✨ Key Highlights (What Makes This Stand Out)

| Feature | Details |
|---|---|
| 🔐 **JWT Auth + Role-Based Access** | Stateless authentication with role-scoped endpoints (ADMIN / DOCTOR / PATIENT) |
| 📧 **Email OTP Verification** | 6-digit OTP sent via email before registration; 10-minute expiry, single-use, auto-cleared on resend |
| 🌐 **Google OAuth2 Social Login** | Patients and doctors can sign in with Google via Spring OAuth2 client |
| 💳 **Razorpay Payment Gateway** | Real, verified online payments for appointment billing — UPI, Cards & Netbanking, with server-side signature verification |
| ☁️ **Cloudinary Cloud Media Storage** | Profile pictures uploaded via multipart requests are validated, streamed, and persisted to Cloudinary — no local disk dependency, fully production-portable |
| 🛡️ **Global Exception Handler** | `@RestControllerAdvice` catches all exceptions — validation, auth, not-found, duplicates — and returns consistent JSON error responses with timestamp |
| ✅ **Bean Validation** | `@Valid` + Jakarta Validation annotations (`@NotNull`, `@NotBlank`, `@Email`, `@Digits`) on all request DTOs |
| 🩺 **Doctor Approval Workflow** | Doctors register but are locked out until an Admin approves their account |
| 🔑 **Password Reset Flow** | Forgot-password → token generation → reset-password via secure token |
| 💰 **Billing & Revenue Module** | Appointments auto-generate billing records; Admin can view daily/monthly revenue stats |
| 📁 **Role-Aware File Management** | Multipart profile picture upload/retrieval shared across PATIENT and DOCTOR roles, backed by Cloudinary |
| 🔔 **Real-time Appointment Notifications** | Dual-channel notifications (in-app + email) for appointment creation & status tracking; includes time & reason details |
| 📬 **Notification Entity** | In-app notification system with read/unread tracking and multi-type support (Appointment, Prescription, Payment, Registration) |
| 📚 **Swagger / OpenAPI Docs** | Auto-generated interactive API docs via SpringDoc OpenAPI 2.5 |
| 🌍 **CORS Configured** | Whitelisted for React frontend at `localhost:5173` and `localhost:3000` via `allowedOriginPatterns`, safely combined with credentialed requests |
| ⚡ **Stateless Sessions** | `SessionCreationPolicy.STATELESS` — no server-side session state |

---

## 🏛️ Architecture

![Architecture Diagram](https://raw.githubusercontent.com/ankitdoi-coder/HealthCare-Backend/main/Requirements%20&%20Architecture/06_Architecture_workflow.png)

Classic **3-tier layered architecture**:

```
Controller (REST API)  →  Service (Business Logic)  →  Repository (JPA / MySQL)
```

The codebase is organized by **domain modules** (feature-based packaging), not by layer — keeping related code co-located and the project scalable.

```
com.ankit.HealthCare_Backend/
├── appointment/          # Appointment booking, status updates
├── authentication/       # JWT, OAuth2, Security config, Auth endpoints
├── billing/              # Billing records, payment, revenue stats, Razorpay integration
├── communication/        # Contact Us feature
├── core/                 # Shared enums (AppointmentStatus, BillingStatus), Role entity
├── Exception/            # GlobalExceptionHandler + custom exceptions
├── filemanagement/       # Profile picture upload/retrieval, Cloudinary integration
├── Notification/         # Notification entity & repository
├── prescription/         # Doctor prescriptions
└── usermanagement/       # Admin, Doctor, Patient, User, Profile sub-modules
```

---

## 🚀 Technology Stack

<div align="center">

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-6DB33F?style=flat-square&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6.5.7-6DB33F?style=flat-square&logo=springsecurity&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-jjwt%200.11.5-000000?style=flat-square&logo=jsonwebtokens&logoColor=white)
![OAuth2](https://img.shields.io/badge/OAuth2-Google-4285F4?style=flat-square&logo=google&logoColor=white)
![Razorpay](https://img.shields.io/badge/Razorpay-Java%20SDK-0C2451?style=flat-square&logo=razorpay&logoColor=white)
![Cloudinary](https://img.shields.io/badge/Cloudinary-Java%20SDK-3448C5?style=flat-square&logo=cloudinary&logoColor=white)
![JavaMail](https://img.shields.io/badge/Email-JavaMailSender-D14836?style=flat-square&logo=gmail&logoColor=white)
![Hibernate](https://img.shields.io/badge/ORM-JPA%2FHibernate-59666C?style=flat-square&logo=hibernate&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.3.0-4479A1?style=flat-square&logo=mysql&logoColor=white)
![Validation](https://img.shields.io/badge/Validation-Jakarta%20Bean-2396F3?style=flat-square&logo=hibernate&logoColor=white)
![Swagger](https://img.shields.io/badge/API%20Docs-SpringDoc%20OpenAPI%202.5.0-85EA2D?style=flat-square&logo=swagger&logoColor=black)
![Maven](https://img.shields.io/badge/Build-Maven-C71A36?style=flat-square&logo=apachemaven&logoColor=white)
![Lombok](https://img.shields.io/badge/Utility-Lombok%201.18.32-BC0032?style=flat-square&logo=lombok&logoColor=white)

</div>

| Component | Technology | Version |
|---|---|---|
| Framework | Spring Boot | 3.5.7 |
| Security | Spring Security + JWT (jjwt) | 6.5.7 / 0.11.5 |
| Social Login | Spring OAuth2 Client (Google) | 6.5.7 |
| Payment Gateway | Razorpay Java SDK | Latest stable |
| Media Storage | Cloudinary Java SDK | Latest stable |
| Email | Spring Boot Starter Mail (JavaMailSender) | 3.5.7 |
| ORM | Spring Data JPA + Hibernate | 3.5.7 |
| Database | MySQL (mysql-connector-j) | 8.3.0 |
| Validation | Spring Boot Starter Validation (Jakarta) | 3.5.7 |
| API Docs | SpringDoc OpenAPI (Swagger UI) | 2.5.0 |
| Build | Maven | 3.x |
| Utilities | Lombok | 1.18.32 |
| Language | Java | 17 |

---

## 🔒 Security Implementation

```
Request → JwtFilter → Validate Token → Set SecurityContext → @PreAuthorize / hasRole()
```

1. **Registration** — `POST /api/auth/register` with full Bean Validation (`@Valid`)
2. **Login** — `POST /api/auth/login` returns a signed JWT; doctors blocked until approved
3. **Google OAuth2** — `/oauth2/**` flow handled by `OAuth2LoginSuccessHandler`, redirects with token
4. **JWT Filter** — `JwtFilter` intercepts every request, validates signature & expiry
5. **Role Guards** — `/api/patient/**` → `ROLE_PATIENT`, `/api/doctor/**` → `ROLE_DOCTOR`, `/api/admin/**` → `ROLE_ADMIN`, `/api/profile/**` → `ROLE_PATIENT` **or** `ROLE_DOCTOR` via `hasAnyRole`
6. **Email OTP** — `POST /api/auth/send-otp` sends a 6-digit OTP; `POST /api/auth/verify-otp` validates it before allowing registration
7. **Password Reset** — Secure time-limited token flow via `POST /api/auth/forgot-password` → `POST /api/auth/reset-password`
8. **BCrypt** — All passwords hashed with `BCryptPasswordEncoder`
9. **Payment Signature Verification** — Every Razorpay payment is verified server-side via HMAC signature before billing status changes — the client can never self-report a payment as successful
10. **Credential-Safe CORS** — `CorsConfigurationSource` uses `allowedOriginPatterns` (never a bare `"*"`) so credentialed requests (JWT-bearing) from the frontend are honored without violating the CORS spec

---

## ☁️ Cloudinary — Cloud-Based Profile Picture Management

Profile pictures for both **Patients** and **Doctors** are uploaded directly to **Cloudinary** rather than local disk — meaning the API remains stateless and horizontally scalable (no shared filesystem needed across instances), and images are served from Cloudinary's CDN.

### 📌 What's Implemented

| Capability | Status |
|---|---|
| Multipart image upload (`multipart/form-data`) | ✅ Implemented |
| Content-type validation — only `image/*` accepted | ✅ Implemented |
| File size validation — 5MB max, rejected before upload | ✅ Implemented |
| Direct stream-to-Cloudinary upload (no local temp storage) | ✅ Implemented |
| Role-aware persistence — updates `Patient` or `Doctor` entity based on logged-in user's role | ✅ Implemented |
| Returns CDN-backed image URL in response for immediate frontend use | ✅ Implemented |

### 🧠 Upload Flow

```
1. Client sends multipart request  ──▶  POST /api/profile/upload-image  (field: profilePicture)
2. JwtFilter authenticates          ──▶  Principal resolved to logged-in user
3. ProfileService validates file    ──▶  content-type check + 5MB size check
4. CloudinaryService.uploadImage()  ──▶  streams file directly to Cloudinary, returns secure URL
5. Service resolves role            ──▶  PATIENT → Patient entity, DOCTOR → Doctor entity
6. profilePicture column updated    ──▶  persisted via @Transactional save
7. Response                         ──▶  { success, message, imageUrl }
```

Because the upload is wrapped in `@Transactional`, a failure at any stage (invalid file, Cloudinary error, DB save error) rolls back cleanly rather than leaving a partially-updated profile.

---

## 💳 Payment Gateway Integration (Razorpay)

The billing module integrates **Razorpay** end-to-end for appointment payments — not a mocked checkout, but a real gateway integration with proper order lifecycle and server-side trust boundaries.

### 📌 What's Implemented

| Capability | Status |
|---|---|
| Order creation via backend (`POST /api/patient/payments/create-order`) | ✅ Implemented |
| Razorpay Checkout (UPI, Cards, Netbanking) | ✅ Implemented |
| Server-side payment signature verification (HMAC-SHA256) | ✅ Implemented |
| Real-time billing status sync (`UNPAID` → `PAID`) | ✅ Implemented |
| Payment failure & checkout-dismissal handling | ✅ Implemented |
| Revenue reporting (daily / monthly) | ✅ Implemented |

### 🧠 Why It's Built This Way

A naive integration trusts the frontend to say "payment succeeded." This one doesn't. The flow is:

```
1. Frontend requests an order  ──▶  Backend calls Razorpay Orders API, returns order_id
2. Razorpay Checkout opens     ──▶  User pays via UPI / Card / Netbanking
3. Razorpay returns            ──▶  payment_id, order_id, signature  (to frontend)
4. Frontend forwards these     ──▶  Backend verification endpoint
5. Backend recomputes HMAC     ──▶  using Razorpay key secret
6. Only on signature match     ──▶  Billing status flips to PAID
```

This is the same trust model used by real fintech and healthtech platforms — the **backend is the single source of truth** for what counts as a successful payment, never the client.

### 🧪 Testing the Payment Flow (Test Mode)

Razorpay's Test Mode sandbox reproduces the entire checkout, OTP, and verification flow with zero real money movement — used to validate this integration end-to-end.

**Card Payment**

| Field | Test Value |
|---|---|
| Card Number | `4111 1111 1111 1111` (Visa) or `5267 3181 8797 5449` (Mastercard) |
| Expiry (MM/YY) | Any future date — e.g. `12/30` |
| CVV | Any 3 digits — e.g. `123` |
| Cardholder Name | Any name |
| OTP | Any 4–10 digit number — e.g. `1234` |

Select **Success** on Razorpay's mock bank page to complete the simulated transaction, or use card `4000 0000 0000 0002` and select **Failure** to test the failure path.

**UPI Payment**

| Field | Test Value |
|---|---|
| UPI ID | `success@razorpay` |

No need to scan the on-screen QR with a real device — that only resolves against live, NPCI-registered transactions. Entering the test UPI ID simulates an instant successful payment in sandbox mode.

Going live requires only swapping the test key (`rzp_test_...`) for a live key (`rzp_live_...`) post KYC/activation on Razorpay's dashboard — no changes to the integration logic itself.

---

## 🛡️ Global Exception Handling

A single `@RestControllerAdvice` class handles all error scenarios and returns a **consistent JSON error envelope**:

```json
{
  "timestamp": "2025-10-27T14:32:10.123",
  "status": 404,
  "message": "Doctor with id 5 not found"
}
```

| Exception | HTTP Status |
|---|---|
| `ResourceNotFoundException` | `404 Not Found` |
| `DuplicateResourceException` | `409 Conflict` |
| `UnauthorizedException` | `401 Unauthorized` |
| `IllegalArgumentException` | `400 Bad Request` |
| `MethodArgumentNotValidException` | `400 Bad Request` (validation errors) |
| `PaymentVerificationException` | `400 Bad Request` (Razorpay signature mismatch) |
| `Exception` (fallback) | `500 Internal Server Error` |

---

## ✅ Request Validation

All incoming request DTOs are validated with **Jakarta Bean Validation** annotations before reaching the service layer:

```java
// RegisterRequestDTO example
@NotNull @NotBlank @Email        private String email;
@NotNull @NotBlank               private String password;
@Digits(integer=10, fraction=0)  private Long contactNumber;

// AppointmentDTO example
@NotNull  private Long patientId;
@NotNull  private Long doctorId;
@NotNull  private LocalDate appointmentDate;

// PaymentVerificationDTO example
@NotNull @NotBlank  private String razorpayOrderId;
@NotNull @NotBlank  private String razorpayPaymentId;
@NotNull @NotBlank  private String razorpaySignature;
@NotNull             private Long appointmentId;
```

Validation failures are caught by the Global Exception Handler and returned as structured `400 Bad Request` responses.

---

## 📋 API Endpoints

### 🔐 Auth — `/api/auth`
| Method | Endpoint | Description | Auth |
|---|---|---|---|
| POST | `/send-otp` | Send 6-digit OTP to email for verification | Public |
| POST | `/verify-otp` | Verify the OTP before registration | Public |
| POST | `/register` | Register a new user (Patient/Doctor) | Public |
| POST | `/login` | Authenticate and receive JWT | Public |
| POST | `/forgot-password` | Request a password reset token | Public |
| POST | `/reset-password` | Reset password using token | Public |
| GET | `/oauth2/callback` | Google OAuth2 redirect handler | Public |

### 🧑‍⚕️ Patient — `/api/patient` _(ROLE_PATIENT)_
| Method | Endpoint | Description |
|---|---|---|
| GET | `/doctors` | Browse all approved doctors |
| POST | `/appointments/new` | Book a new appointment |
| GET | `/appointments/my` | View personal appointment history |
| DELETE | `/appointments/{id}/cancel` | Cancel an appointment |
| PUT | `/appointments/{id}/pay` | Make payment for an appointment |
| POST | `/payments/create-order` | Create a Razorpay order for an appointment |
| POST | `/payments/verify` | Verify Razorpay payment signature and mark billing as `PAID` |
| GET | `/prescriptions` | View personal prescriptions |
| GET | `/profile` | View own patient profile (includes `profilePicture` URL) |

### 👨‍⚕️ Doctor — `/api/doctor` _(ROLE_DOCTOR)_
| Method | Endpoint | Description |
|---|---|---|
| GET | `/profile` | View own doctor profile (includes `profilePicture` URL) |
| GET | `/appointments/my` | View all own appointments |
| PUT | `/appointments/{id}/status` | Update appointment status (triggers in-app + email notifications) |
| GET | `/patients` | View all own patients |
| POST | `/prescription` | Create a prescription |
| GET | `/prescriptions` | View all own prescriptions |

### 🖼️ Profile / File Management — `/api/profile` _(ROLE_PATIENT or ROLE_DOCTOR)_
| Method | Endpoint | Description |
|---|---|---|
| POST | `/upload-image` | Upload a profile picture (`multipart/form-data`, field: `profilePicture`) — validated, streamed to Cloudinary, and linked to the caller's Patient or Doctor record |
| DELETE | `/delete-image` | Remove the current profile picture |

### 🛠️ Admin — `/api/admin` _(ROLE_ADMIN)_
| Method | Endpoint | Description |
|---|---|---|
| GET | `/doctors` | Get all doctors (including pending) |
| PUT | `/doctors/{id}/approve` | Approve a doctor |
| PUT | `/doctors/{id}/reject` | Reject / revoke a doctor |
| GET | `/patients` | Get all patients |
| GET | `/billing` | View all billing records |
| PUT | `/billing/{id}/status` | Update a billing record's status |
| GET | `/revenue/daily` | Get today's total revenue |
| GET | `/revenue/monthly` | Get current month's total revenue |

### 🔔 Notifications — `/api/notifications` _(ROLE_PATIENT / ROLE_DOCTOR)_
| Method | Endpoint | Description |
|---|---|---|
| GET | `/my` | Get all notifications for logged-in user (newest first) |
| GET | `/unread-count` | Get count of unread notifications (for UI badge) |
| PUT | `/{id}/read` | Mark a specific notification as read |
| PUT | `/mark-all-read` | Mark all unread notifications as read |

---

## 🗄️ Database Schema

![ER Diagram](https://raw.githubusercontent.com/ankitdoi-coder/HealthCare-Backend/main/Requirements%20&%20Architecture/04_ERD_DB.jpg)

Core entities: `User`, `Role`, `Patient`, `Doctor`, `Admin`, `Appointment`, `Prescription`, `Billing`, `ContactUs`, `Notification`, `PasswordResetToken`

`Billing` stores the Razorpay `orderId`, `paymentId`, and `status` (`UNPAID` / `PAID`) per appointment, giving a full payment audit trail per record.

`Patient` and `Doctor` each store a `profilePicture` column holding the Cloudinary-hosted CDN URL of the user's uploaded profile image.

---

## 📖 API Documentation (Swagger)

Once running, the interactive Swagger UI is available at:

```
http://localhost:8080/swagger-ui/index.html
```

---

## ⚙️ Getting Started

### ✅ Prerequisites
- Java 17+
- Maven 3.x
- MySQL 8.x
- A Razorpay account (Test Mode keys are free — no business verification needed to start testing)
- A Cloudinary account (free tier is sufficient for development)

### 🛠️ Setup

```bash
git clone https://github.com/ankitdoi-coder/healthcare-backend.git
cd healthcare-backend
```

Create the database:
```sql
CREATE DATABASE healthcaredb;
```

Configure `src/main/resources/application.properties`:
```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
app.jwt.secret=${JWT_SECRET}
app.jwt.expiration=${JWT_EXPIRATION_MS}
razorpay.key.id=${RAZORPAY_KEY_ID}
razorpay.key.secret=${RAZORPAY_KEY_SECRET}
cloudinary.cloud-name=${CLOUDINARY_CLOUD_NAME}
cloudinary.api-key=${CLOUDINARY_API_KEY}
cloudinary.api-secret=${CLOUDINARY_API_SECRET}
```

Run:
```bash
mvn spring-boot:run
```

Server starts at `http://localhost:8080`

---

## 🔧 Environment Variables

| Variable | Description | Example |
|---|---|---|
| `DB_URL` | JDBC connection URL | `jdbc:mysql://localhost:3306/healthcaredb` |
| `DB_USERNAME` | Database username | `root` |
| `DB_PASSWORD` | Database password | `your_password` |
| `JWT_SECRET` | Secret key for signing JWTs | `a-very-long-random-secret-key` |
| `JWT_EXPIRATION_MS` | Token TTL in milliseconds | `86400000` (24h) |
| `RAZORPAY_KEY_ID` | Razorpay API Key ID (test or live) | `rzp_test_xxxxxxxxxxxx` |
| `RAZORPAY_KEY_SECRET` | Razorpay API Key Secret (test or live) | `your_razorpay_key_secret` |
| `CLOUDINARY_CLOUD_NAME` | Cloudinary account cloud name | `your_cloud_name` |
| `CLOUDINARY_API_KEY` | Cloudinary API key | `123456789012345` |
| `CLOUDINARY_API_SECRET` | Cloudinary API secret | `your_cloudinary_secret` |

---

## 🔔 Real-time Appointment Notifications

The system sends **dual-channel notifications** (in-app + email) for all key appointment events. Notifications include appointment time and reason details for full context.

### 📅 Appointment Creation Notification
When a patient books an appointment:

**In-App Notification** (stored in database)
- Sent to: Patient & Doctor
- Message: "Your Appointment Booked with Doctor: [Name]" (Patient) / "You have new Appointment from Patient: [Name]" (Doctor)
- Type: `APPOINTMENT`
- Read/Unread tracking enabled

**Email Notification** (via JavaMailSender)
- **Patient receives**: Appointment confirmation with doctor name, date, and gratitude message
- **Doctor receives**: Appointment alert with patient name, scheduled date, and dashboard reminder
- Both emails include appointment time (`LocalTime`) and reason for visit details

### 🔄 Appointment Status Update Notification
When a doctor updates the appointment status (SCHEDULED → COMPLETED / CANCELED, etc.):

**In-App Notification** (to patient)
- Message: "Your appointment status has been updated to: [STATUS] by Dr. [Name]"
- Type: `APPOINTMENT`

**Email Notification** (to patient)
- Subject: "Appointment Status Update"
- Contains: Appointment date, doctor name, new status, and dashboard link

### 📬 Notification Management API
Patients and doctors can:
- Retrieve all notifications sorted by creation date (newest first)
- Check unread notification count (for UI bell badge)
- Mark individual notifications as read
- Mark all notifications as read in one call

### 🏷️ Notification Types
- `APPOINTMENT` — Appointment booking and status changes
- `PRESCRIPTION` — Prescription-related (extensible for future use)
- `PAYMENT` — Payment status updates (extensible for future use)
- `REGISTRATION` — Account registration events (extensible for future use)

---

## 👀 For Reviewers

This project was built to demonstrate practical, production-grade backend engineering rather than tutorial-level CRUD:

- 🔐 **Security-first payment handling** — billing status is never trusted from the client; it's gated behind server-side HMAC signature verification, mirroring real fintech/healthtech systems.
- ☁️ **Stateless media handling** — profile pictures stream directly to Cloudinary rather than local disk, keeping the API instance-agnostic and production-portable from day one.
- 🪪 **Stateless, role-scoped JWT auth** with a proper OAuth2 social login path alongside it.
- 🧩 **Consistent error contracts** across the entire API via a single global exception handler.
- 🏗️ **Domain-driven package structure** that scales cleanly as features are added, rather than a flat MVC layout.
- 🔗 **Real third-party integration experience** with Razorpay's order lifecycle (create → checkout → verify) and Cloudinary's upload API — not simulated or mocked integrations.
- 🎥 **Documented engineering process** — video walkthroughs above show real debugging and design decisions, not just polished final output.

---

<div align="center">

### 👤 Author

**Ankit** — Java Full Stack Developer

[![GitHub](https://img.shields.io/badge/GitHub-ankitdoi--coder-181717?style=flat-square&logo=github&logoColor=white)](https://github.com/ankitdoi-coder)

💼 *Open to Java Full Stack / Backend / Frontend opportunities. Feel free to connect!*

</div>