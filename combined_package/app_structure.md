# App Structure and Navigation Flow

## Main Components
1. Splash Screen
2. Dashboard/Home Screen
3. Product Management
4. Billing System
5. Sales Analysis
6. Profit Analysis

## Navigation Flow
- Splash Screen → Dashboard
- Dashboard → All main features accessible via icons/cards

## Screen Layouts

### Splash Screen
- Full screen with centered "Dastak Mobile" logo/text with fade-in animation
- "Developed by Aaryan7" at bottom with slide-up animation
- Auto-transition to Dashboard after 3 seconds

### Dashboard/Home Screen
- Grid layout with large, clearly labeled icons for each main feature:
  - Add Product
  - My Products
  - Create Bill
  - Sales Analysis
  - Profit Analysis
- Shop name "Dastak Mobile" in header
- Quick stats summary (today's sales, total products, etc.)

### Product Management
- Add Product Screen:
  - Form with fields for:
    - Product Name
    - Purchase Price
    - Selling Price
  - Auto-calculation of profit
  - Save button
  
- My Products Screen:
  - List view of all products with search functionality
  - Each product shows name, selling price, and profit
  - Swipe actions for edit/delete
  - Tap to view full details

### Billing System
- Create Bill Screen:
  - Two tabs: "Select Products" and "Manual Entry"
  - Select Products tab:
    - Searchable product list
    - Quantity selector
    - Add to bill button
  - Manual Entry tab:
    - Form to enter product name and price
    - Add to bill button
  - Current bill preview at bottom
  - Generate PDF button
  
- Bill Preview/PDF:
  - Shop header with "Dastak Mobile"
  - Date and bill number
  - Itemized list of products with quantities and prices
  - Subtotal, tax (if applicable), and total
  - "Thank you for shopping" message

### Sales Analysis
- Main Analysis Screen:
  - Tabs for Day/Week/Month/Year views
  - Graph visualization of sales trends
  - Total sales figures
  - Best-selling products

### Profit Analysis
- Profit Overview Screen:
  - Total profit calculation
  - Profit margin percentage
  - Most profitable products
  - Profit trends over time (graph)
