export const FORM_ERROR_MESSAGES = {
  name: 'Name is required.',
  category: 'Category is required.',
  description: 'Description is required.',
  price: 'Price must be at least 0.01.',
  quantity: 'Quantity must be at least 1.',
} as const;

export const FORM_PLACEHOLDERS = {
  name: '100 characters max',
  category: '50 characters max',
  description: '255 characters max',
  price: 'Enter product price. E.g., 19.99',
  quantity: 'Enter product quantity. E.g., 1',
} as const;

export const FORM_LABELS = {
  name: 'Product Name',
  category: 'Category',
  description: 'Description',
  price: 'Price',
  quantity: 'Quantity',
} as const;
