## PartFlow Modernization Plan (UI/UX + Functionality)

### Vision
- Build a fast, reliable desktop app with a minimalist, modern UI that’s easy to learn and efficient for daily use.
- Prioritize clarity, consistency, and performance; reduce visual noise and cognitive load.

### Design Principles
- Minimalist layout, ample whitespace, clear visual hierarchy
- Consistent patterns and naming, predictable navigation
- Accessibility (keyboard support, contrast, focus states)
- Responsive layouts for small/large windows
- Clear error and loading states; optimistic UI where safe

## UI/UX Redesign

### 1) Visual Language
- Color palette (neutral, calm, high-contrast where needed)
  - Primary: Indigo 600 `#4F46E5` (hover: `#4338CA`)
  - Accent: Emerald 500 `#10B981`
  - Neutral BG: `#F8FAFC` / Surfaces: `#FFFFFF` / Borders: `#E5E7EB`
  - Text: `#111827` (primary), `#6B7280` (secondary), Error `#DC2626`
- Typography: Segoe UI (Windows default) or Inter; sizes: 12/14/16/18/24/28
- Spacing scale: 4/8/12/16/20/24 px; use consistently in FXML and CSS
- Elevation: subtle shadows only for dialogs/menus; avoid heavy borders

### 2) Layout & Navigation
- App shell: persistent left sidebar with active state, content area on the right
- Top content header with page title and primary action(s)
- Consistent page templates: header → filters/actions → table/cards → pagination
- Keyboard shortcuts for major screens (Alt+1 Dashboard, Alt+2 Inventory, …)
- Ensure logout replaces the entire Scene (no sidebar remnants)

### 3) Components (JavaFX CSS + FXML)
- Buttons: primary, secondary, subtle; clear hover/disabled states
- Inputs: text, number, combo; validation states (success/error/helper text)
- TableView: dense and comfortable modes, column alignment, sticky header
- Dialogs: modern modal for create/edit; avoid comma-separated text inputs
- Toasts/inline alerts: success/error/info patterns
- Empty states and skeleton loaders for data-heavy views

### 4) Theming & Dark Mode (Phase 2)
- Centralize tokens in `style.css`; class-based theming for light/dark
- Optional dark palette: Background `#0B1220`, Surfaces `#111827`, Text `#E5E7EB`, Primary `#6366F1`

### 5) Accessibility
- Focus outlines, logical tab order, ARIA-like semantics where possible
- Color contrast AA for text and interactive elements

## Functional Enhancements

### 1) Authentication & Authorization
- Passwords: BCrypt hashing; remove plaintext handling
- Roles: `ADMIN`, `STAFF`, `VIEWER`; restrict menus/actions by role
- Session/logout: clear state, return to login, hide sidebar/navigation

### 2) Inventory Management
- CRUD with form dialogs (Create/Edit); validation (numeric price/qty, non-negative)
- SKU/Part Number uniqueness; vendor association via ComboBox
- Search, sort, filter, pagination for large datasets
- Bulk import/export (CSV); optional barcode/QR later
- Low stock alerts with thresholds; quick actions to create restock orders

### 3) Vendor Management
- CRUD with validation (email/phone), deduplication by name/contact
- Search/filter by name/contact; pagination
- Link vendor to parts; display vendor health (fulfillment rate later)

### 4) Sales Management
- Create sale: select part(s), quantity, auto-calc totals; decrement stock
- Edit/void sale with audit trail (role-restricted)
- Receipt generation (printable/PDF)

### 5) Restocking
- Restock Order lifecycle: Draft → Sent → Partially Received → Completed → Cancelled
- Generate PO (printable/PDF); email to vendor (future)
- Receive items: update stock and costs; reconcile partial receipts
- Suggested restock list from low-stock thresholds

### 6) Reporting & Dashboard
- Time-based filters: today/week/month/custom; CSV/PDF exports
- KPIs on dashboard: Inventory value, Low-stock count, Sales (period), Top items
- Charts (later): sales trend, stock turnover; keep minimal and readable

### 7) Data & Integrity
- Entity constraints: not-null, unique indexes (part number, vendor name)
- Database migrations via Flyway/Liquibase (Phase 2)
- Seed data for demo and testing paths

### 8) Error Handling & Observability
- Centralized exception handling with user-friendly alerts
- Logging strategy (INFO for business flow, DEBUG for development)
- Optional audit log for sensitive changes (user/role, void sales)

### 9) Performance
- Table pagination and server-side filtering (where needed)
- Proper repository queries and indexes; avoid N+1 fetches

### 10) Testing & Quality
- Unit tests: services, repositories, validators
- Integration tests: Spring Boot + H2
- UI smoke tests (optional) with TestFX
- Pre-commit checks: format/lint/test (via CI)

### 11) Build, Packaging, CI/CD
- Java 21 + Spring Boot + JavaFX
- Packaging: jlink/jpackage for platform-specific installers
- GitHub Actions: build, test, package artifacts on push

## Roadmap (Milestones & Acceptance)

### Milestone 1: Stabilize Current App (1–2 days)
- Fix navigation and logout scene swap issues
- Align FXML ids with controllers; remove legacy handlers
- Basic validation on Inventory/Vendors forms
- Acceptance: app builds, runs; sidebar/nav stable; no obvious runtime errors

### Milestone 2: Minimalist UI Refresh (3–5 days)
- Implement new palette, spacing, and typography in `style.css`
- Update `Login.fxml`, `Sidebar.fxml`, `Dashboard.fxml` to template
- Introduce modal forms for create/edit in Inventory/Vendors
- Acceptance: visually consistent, clear states, accessible focus/contrast

### Milestone 3: Functional Completeness (5–8 days)
- Inventory/Vendors CRUD finalized with validation + search/filter/pagination
- Sales flow with stock decrement; basic reports export CSV
- Restock order draft → receive flow; low-stock suggestions
- Acceptance: end-to-end flows tested with sample data

### Milestone 4: Security & Quality (3–5 days)
- BCrypt passwords, roles/permissions, restricted menus/actions
- Add unit/integration tests; CI pipeline
- Acceptance: all tests green, role-based UX differences verified

### Milestone 5: Polish & Packaging (2–4 days)
- Error/empty/loading states; toasts; minor UX refinements
- Optional dark mode; jpackage installers; release notes
- Acceptance: packaged app install/run; UX meets minimalist goals

## Deliverables
- Updated FXML/CSS implementing minimalist UI
- Validated CRUD flows for Inventory, Vendors, Sales, Restocking
- Reporting exports; dashboard KPIs
- Auth with roles; tests; CI pipeline; packaging artifacts

## Risks & Mitigations
- Binary DB in VCS: consider migrating to migrations + seed data
- Mixed FXML/controller patterns: standardize injection, fx:ids, handlers
- Scope creep: adhere to milestones; defer advanced charts/emails to later

## Success Criteria
- Users can complete daily tasks quickly with fewer clicks
- UI is visually clean, consistent, accessible, and responsive
- Core flows (Inventory, Vendors, Sales, Restocking, Reports) reliable
- Build, tests, and packaged releases are automated and reproducible