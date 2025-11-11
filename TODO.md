# TODO List for Billing System Updates

## 1. Link Signup and Login to Database
- [x] Update SignupController to save new users to DB using UserService.saveUser
- [x] Update LoginController to authenticate users using UserService.loginUser
- [x] Handle user ID generation in SignupController

## 2. Fetch Dashboard Data from Database
- [x] Update DashboardController to fetch today's sales from SalesDao
- [x] Fetch items sold count from SalesItemsDao or SalesDao
- [x] Fetch low stock products from ProductsDao
- [x] Fetch active customers count from CustomerDao
- [x] Populate sales pie chart with data from SalesDao (by payment method or category)
- [x] Populate sales line chart with daily sales data from SalesDao
- [x] Fetch recent transactions from SalesDao
- [x] Fetch low stock list from ProductsDao

## 3. Adjust POS Logic for Payment Scenarios
- [x] Modify PosController.finalizeSale() to:
  - If fully paid: Set customer to "WALKIN", generate receipt only
  - If partially paid: Prompt for customer selection, generate receipt for paid amount and invoice for unpaid
  - If no payment: Prompt for customer selection, generate invoice only (no receipt)
- [x] Ensure customer selection dialog is used appropriately
- [x] Update receipt and invoice creation logic accordingly

## 4. Testing and Verification
- [ ] Test signup and login functionality with DB
- [ ] Verify dashboard loads real data
- [ ] Test POS scenarios: full payment, partial payment, no payment
- [ ] Check receipt and invoice generation
