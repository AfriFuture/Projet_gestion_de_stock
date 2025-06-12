USE gestion_stock;

INSERT INTO produit (nom, categorie, prix_unitaire, quantite_en_stock, image, description, seuil_stock) VALUES
('Laptop', 'Electronics', 350000, 10, 'laptop.jpg', '15-inch professional laptop', 5),
('Mouse', 'Accessories', 7500, 25, 'mouse.jpg', 'Wireless ergonomic mouse', 10),
('Notebook', 'Stationery', 1500, 100, 'notebook.jpg', 'A5 spiral notebook', 20),
('Desk Lamp', 'Furniture', 12000, 4, 'lamp.jpg', 'LED desk lamp with USB port', 5),
('Smartphone', 'Electronics', 250000, 8, 'smartphone.jpg', 'Latest Android phone', 5);
