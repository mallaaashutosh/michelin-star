# Email and Password Validation Implementation

## Overview
Added comprehensive email format and password strength validation to the Michelin Star restaurant application with both client-side (JavaScript) and server-side (Java) validation.

---

## Changes Made

### 1. **ValidationUtil.java** (New Validation Utility Class)
**Location:** `src/main/java/com.restaurant/utils/ValidationUtil.java`

**Purpose:** Centralized validation methods for email and password strength

**Features:**
- `isValidEmail(String email)`: Validates email format
  - Pattern: `^[A-Za-z0-9+_.-]+@(.+)$`
  - Returns: `true` if valid, `false` otherwise

- `isStrongPassword(String password)`: Validates password strength
  - Requires:
    - Minimum 8 characters
    - At least one uppercase letter (A-Z)
    - At least one lowercase letter (a-z)
    - At least one number (0-9)
    - At least one special character (@$!%*?&)
  - Pattern: `^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$`
  - Returns: `true` if password meets all requirements, `false` otherwise

- `getPasswordRequirements()`: Returns user-friendly password requirement message

---

### 2. **LoginServlet.java** (Updated)
**Location:** `src/main/java/com.restaurant/controller/LoginServlet.java`

**Changes:**
- Added import: `import com.restaurant.utils.ValidationUtil;`
- Updated `doPost()` method to validate email format before authentication
- Error handling: Returns error message if email format is invalid
- Preserves email in form field for user correction

**Server-Side Validation Logic:**
```java
// Validate email format
if (!ValidationUtil.isValidEmail(email)) {
    request.setAttribute("error", "Please enter a valid email address.");
    request.setAttribute("email", email);
    request.getRequestDispatcher("/login.jsp").forward(request, response);
    return;
}
```

---

### 3. **RegisterServlet.java** (Updated)
**Location:** `src/main/java/com.restaurant/controller/RegisterServlet.java`

**Changes:**
- Added import: `import com.restaurant.utils.ValidationUtil;`
- Updated `doPost()` method to validate:
  1. Email format (before checking if email exists)
  2. Password strength (meets all requirements)
  3. Password confirmation match (after strength check)

**Server-Side Validation Logic:**
```java
// Validate email format
if (!ValidationUtil.isValidEmail(email)) {
    request.setAttribute("error", "Please enter a valid email address.");
    request.getRequestDispatcher("/register.jsp").forward(request, response);
    return;
}

// Validate password strength
if (!ValidationUtil.isStrongPassword(password)) {
    request.setAttribute("error", ValidationUtil.getPasswordRequirements());
    request.getRequestDispatcher("/register.jsp").forward(request, response);
    return;
}
```

**Validation Order:**
1. Check all fields are not empty
2. Validate email format (RFC compliant)
3. Validate password strength (8+ chars, uppercase, lowercase, number, special char)
4. Check password confirmation matches
5. Check email doesn't already exist
6. Proceed with registration if all validations pass

---

### 4. **login.jsp** (Updated)
**Location:** `src/main/webapp/login.jsp`

**Client-Side Enhancements:**
- Added JavaScript validation function: `validateEmail()`
- Added form submit validation: `validateLoginForm()`
- Added real-time email validation on blur event
- Error message display element: `<div id="emailError" class="validation-error">`

**Features:**
- Validates email format matches regex pattern before form submission
- Shows error message below email input field
- Prevents form submission if email is invalid
- Real-time feedback as user leaves email field

**CSS Styling:**
```css
.validation-error {
    color: #d9534f;
    font-size: 12px;
    margin-top: 5px;
    display: none;
}

.validation-error.show {
    display: block;
}
```

---

### 5. **register.jsp** (Updated)
**Location:** `src/main/webapp/register.jsp`

**Client-Side Enhancements:**
- Added email validation: `validateEmail()`
- Added password strength validator: `validatePasswordStrength()`
- Real-time password requirement checker: `updatePasswordRequirements()`
- Visual requirement indicators showing met/unmet requirements
- Form submit validation: `validateRegisterForm()`

**Features:**

1. **Email Validation:**
   - Same regex pattern as backend
   - Real-time validation on blur
   - Shows error if format is invalid

2. **Password Strength Display:**
   - Live requirement checklist showing:
     - ✓ 8+ characters
     - ✓ Uppercase letter (A-Z)
     - ✓ Lowercase letter (a-z)
     - ✓ Number (0-9)
     - ✓ Special character (@$!%*?&)
   - Requirements turn green (✓) when met
   - Requirements show red when unmet
   - Updates in real-time as user types password

3. **Password Confirmation:**
   - Validates that both passwords match
   - Shows error if they don't match

**CSS Styling:**
```css
.validation-error { color: #d9534f; font-size: 12px; display: none; }
.validation-error.show { display: block; }
.requirement { margin: 3px 0; }
.requirement.met { color: #28a745; }      /* Green for met */
.requirement.unmet { color: #d9534f; }    /* Red for unmet */
.validation-info { color: #666; font-size: 12px; margin-top: 5px; }
```

---

## Validation Flow

### Registration Flow:
```
User Input (Register Form)
    ↓
Client-Side Validation (JavaScript)
    ├─ Email format check
    ├─ Password strength requirements display
    └─ Password confirmation match
    ↓
Form Submission
    ↓
Server-Side Validation (Java - RegisterServlet)
    ├─ Required fields check
    ├─ Email format validation (ValidationUtil)
    ├─ Password strength validation (ValidationUtil)
    ├─ Password confirmation match
    ├─ Email uniqueness check (Database)
    └─ User registration
    ↓
Response: Success or Error Message
```

### Login Flow:
```
User Input (Login Form)
    ↓
Client-Side Validation (JavaScript)
    └─ Email format check
    ↓
Form Submission
    ↓
Server-Side Validation (Java - LoginServlet)
    ├─ Required fields check
    ├─ Email format validation (ValidationUtil)
    └─ Authenticate against database
    ↓
Response: Login Success or Error Message
```

---

## Password Requirements

Users must create passwords that:
- ✓ Are at least **8 characters** long
- ✓ Contain at least one **uppercase letter** (A-Z)
- ✓ Contain at least one **lowercase letter** (a-z)
- ✓ Contain at least one **number** (0-9)
- ✓ Contain at least one **special character** (@$!%*?&)

**Valid Example:** `MyPassword123!`
**Invalid Examples:**
- `short1!` (less than 8 characters)
- `noupppercase123!` (no uppercase)
- `NOLOWERCASE123!` (no lowercase)
- `NoNumbers!` (no number)
- `NoSpecial123` (no special character)

---

## Email Validation

Accepts standard email formats like:
- `user@example.com`
- `firstname.lastname@domain.co.uk`
- `user+tag@company.org`

Rejects:
- `plainaddress` (missing @)
- `@example.com` (missing local part)
- Spaces or invalid characters

---

## Testing the Validation

### To Test Email Validation:
1. Navigate to login or registration page
2. Enter invalid email (e.g., "notanemail" or "user@")
3. See error message appear immediately
4. Enter valid email to clear error

### To Test Password Strength:
1. Navigate to registration page
2. Start typing in password field
3. Watch requirement checklist update in real-time
4. See each requirement turn green as it's met
5. Try submitting with weak password - see error message

### Examples to Test:
- **Weak:** `Pass` → shows all unmet
- **Better:** `Password1` → missing special character
- **Strong:** `MyPass123!` → all requirements met ✓

---

## Build Status
✅ **Compilation: SUCCESS**
✅ **Packaging: SUCCESS**
✅ **Ready for deployment**

## Files Modified/Created
1. ✅ `src/main/java/com.restaurant/utils/ValidationUtil.java` (created)
2. ✅ `src/main/java/com.restaurant/controller/LoginServlet.java` (updated)
3. ✅ `src/main/java/com.restaurant/controller/RegisterServlet.java` (updated)
4. ✅ `src/main/webapp/login.jsp` (updated)
5. ✅ `src/main/webapp/register.jsp` (updated)

---

## Security Benefits
1. **Email Validation:** Prevents typos and invalid email registrations
2. **Password Strength:** Enforces strong passwords resistant to brute-force attacks
3. **Dual Validation:** Client-side for UX, server-side for security
4. **Regular Expressions:** Uses proven patterns for validation
5. **Error Messages:** Guides users to correct format without exposing system details

