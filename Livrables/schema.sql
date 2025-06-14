CREATE DATABASE gestion_stock;
USE gestion_stock;
CREATE TABLE IF NOT EXISTS produit (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    categorie VARCHAR(100),
    prix_unitaire DECIMAL(10,2) NOT NULL,
	seuil_stock INT DEFAULT 5,
    quantite_en_stock INT NOT NULL,
    image VARCHAR(255),
    description TEXT
);

CREATE TABLE IF NOT EXISTS commande (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom_client VARCHAR(100) NOT NULL,
    date_commande DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS ligne_commande (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_commande INT NOT NULL,
    id_produit INT NOT NULL,
    quantite INT NOT NULL,
    prix_unitaire DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_commande) REFERENCES commande(id)
        ON DELETE CASCADE,
    FOREIGN KEY (id_produit) REFERENCES produit(id)
);
