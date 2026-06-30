# 🏥 Smart Healthcare System — Backend

A production-ready, secure, and scalable RESTful API for a **Smart Healthcare Appointment & Records System**, built with **Java 17 + Spring Boot 3.5**. Implements real-world engineering practices including JWT-based auth, role-based access control, centralized exception handling, request validation, OAuth2 social login, **Razorpay payment gateway integration**, billing management, file uploads, and automated API documentation.

---

## ✨ Key Highlights (What Makes This Stand Out)

| Feature | Details |
|---|---|
| **JWT Auth + Role-Based Access** | Stateless authentication with role-scoped endpoints (ADMIN / DOCTOR / PATIENT) |
| **Email OTP Verification** | 6-digit OTP sent via email before registration; 10-minute expiry, single-use, auto-cleared on resend |
| **Google OAuth2 Social Login** | Patients and doctors can sign in with Google via Spring OAuth2 client |
| **Razorpay Payment Gateway** | Real, verified online payments for appointment billing — UPI, Cards & Netbanking, with server-side signature verification |
| **Global Exception Handler** | `@RestControllerAdvice` catches all exceptions — validation, auth, not-found, duplicates — and returns consistent JSON error responses with timestamp |
| **Bean Validation** | `@Valid` + Jakarta Validation annotations (`@NotNull`, `@NotBlank`, `@Email`, `@Digits`) on all request DTOs |
| **Doctor Approval Workflow** | Doctors register but are locked out until an Admin approves their account |
| **Password Reset Flow** | Forgot-password → token generation → reset-password via secure token |
| **Billing & Revenue Module** | Appointments auto-generate billing records; Admin can view daily/monthly revenue stats |
| **File Management** | Profile picture upload/retrieval via dedicated file controller |
| **Notification Entity** | Foundation for in-app notifications (NotificationEntity, NotificationRepo) |
| **Swagger / OpenAPI Docs** | Auto-generated interactive API docs via SpringDoc OpenAPI 2.5 |
| **CORS Configured** | Whitelisted for React frontend at `localhost:5173` and `localhost:3000` |
| **Stateless Sessions** | `SessionCreationPolicy.STATELESS` — no server-side session state |

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
├── filemanagement/       # Profile picture upload/retrieval
├── Notification/         # Notification entity & repository
├── prescription/         # Doctor prescriptions
└── usermanagement/       # Admin, Doctor, Patient, User, Profile sub-modules
```

---

## 🚀 Technology Stack

| Component | Technology | Version |
|---|---|---|
| Framework | Spring Boot | 3.5.7 |
| Security | Spring Security + JWT (jjwt) | 6.5.7 / 0.11.5 |
| Social Login | Spring OAuth2 Client (Google) | 6.5.7 |
| Payment Gateway | Razorpay Java SDK | Latest stable |
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
5. **Role Guards** — `/api/patient/**` → `ROLE_PATIENT`, `/api/doctor/**` → `ROLE_DOCTOR`, `/api/admin/**` → `ROLE_ADMIN`
6. **Email OTP** — `POST /api/auth/send-otp` sends a 6-digit OTP; `POST /api/auth/verify-otp` validates it before allowing registration
7. **Password Reset** — Secure time-limited token flow via `POST /api/auth/forgot-password` → `POST /api/auth/reset-password`
8. **BCrypt** — All passwords hashed with `BCryptPasswordEncoder`
9. **Payment Signature Verification** — Every Razorpay payment is verified server-side via HMAC signature before billing status changes — the client can never self-report a payment as successful

---

## 💳 Payment Gateway Integration (Razorpay)

The billing module integrates **Razorpay** end-to-end for appointment payments — not a mocked checkout, but a real gateway integration with proper order lifecycle and server-side trust boundaries.

### What's Implemented

| Capability | Status |
|---|---|
| Order creation via backend (`POST /api/patient/payments/create-order`) | ✅ Implemented |
| Razorpay Checkout (UPI, Cards, Netbanking) | ✅ Implemented |
| Server-side payment signature verification (HMAC-SHA256) | ✅ Implemented |
| Real-time billing status sync (`UNPAID` → `PAID`) | ✅ Implemented |
| Payment failure & checkout-dismissal handling | ✅ Implemented |
| Revenue reporting (daily / monthly) | ✅ Implemented |

### Why It's Built This Way

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

### Testing the Payment Flow (Test Mode)

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
| GET | `/profile` | View own patient profile |

### 👨‍⚕️ Doctor — `/api/doctor` _(ROLE_DOCTOR)_
| Method | Endpoint | Description |
|---|---|---|
| GET | `/profile` | View own doctor profile |
| GET | `/appointments/my` | View all own appointments |
| PUT | `/appointments/{id}/status` | Update appointment status |
| GET | `/patients` | View all own patients |
| POST | `/prescription` | Create a prescription |
| GET | `/prescriptions` | View all own prescriptions |

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

---

## 🗄️ Database Schema

![ER Diagram](https://raw.githubusercontent.com/ankitdoi-coder/HealthCare-Backend/main/Requirements%20&%20Architecture/04_ERD_DB.jpg)

Core entities: `User`, `Role`, `Patient`, `Doctor`, `Admin`, `Appointment`, `Prescription`, `Billing`, `ProfilePicture`, `ContactUs`, `Notification`, `PasswordResetToken`

`Billing` stores the Razorpay `orderId`, `paymentId`, and `status` (`UNPAID` / `PAID`) per appointment, giving a full payment audit trail per record.

---

## 📖 API Documentation (Swagger)

Once running, the interactive Swagger UI is available at:

```
http://localhost:8080/swagger-ui/index.html
```

---

## ⚙️ Getting Started

### Prerequisites
- Java 17+
- Maven 3.x
- MySQL 8.x
- A Razorpay account (Test Mode keys are free — no business verification needed to start testing)

### Setup

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

---

## 👀 For Reviewers

This project was built to demonstrate practical, production-grade backend engineering rather than tutorial-level CRUD:

- **Security-first payment handling** — billing status is never trusted from the client; it's gated behind server-side HMAC signature verification, mirroring real fintech/healthtech systems.
- **Stateless, role-scoped JWT auth** with a proper OAuth2 social login path alongside it.
- **Consistent error contracts** across the entire API via a single global exception handler.
- **Domain-driven package structure** that scales cleanly as features are added, rather than a flat MVC layout.
- **Real third-party integration experience** with Razorpay's order lifecycle (create → checkout → verify), not a simulated or mocked payment button.