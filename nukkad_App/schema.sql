CREATE TABLE IF NOT EXISTS category (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS customer(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255),
    address VARCHAR(255),
    dob VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS item(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    image VARCHAR(255),
    unit VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS item_category(
    item_id INT NOT NULL,
    category_id INT NOT NULL,
    PRIMARY KEY (item_id, category_id),
    FOREIGN KEY (item_id) REFERENCES item(id),
    FOREIGN KEY (category_id) REFERENCES category(id)
);

CREATE TABLE IF NOT EXISTS shopkeeper (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    store_number VARCHAR(255),
    gst_in VARCHAR(255),
    address VARCHAR(255),
    city VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS shopkeeper_pictures (
    shopkeeper_id INT NOT NULL,
    picture_url VARCHAR(1024),
    FOREIGN KEY (shopkeeper_id) REFERENCES shopkeeper(id)
);

CREATE TABLE IF NOT EXISTS addresses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    address_line1 VARCHAR(255),
    address_line2 VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    pincode VARCHAR(20),
    landmark VARCHAR(255),
    label VARCHAR(50),
    is_selected BOOLEAN DEFAULT FALSE,
    customer_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);
--
--CREATE TABLE IF NOT EXISTS orders_status_entity (
--    id BIGINT AUTO_INCREMENT PRIMARY KEY,
--    order_id BIGINT NOT NULL UNIQUE,
--    item_id BIGINT NOT NULL ,
--    quantity INT NOT NULL,
--    status VARCHAR(20)
--
--      INDEX idx_item_id (item_id),
--      INDEX idx_status (status)
--);

CREATE TABLE IF NOT EXISTS order_entity (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cart_id INT NOT NULL,
    customer_id INT NOT NULL,
    delivery_address_id INT NOT NULL,
    store_keeper_id INT NOT NULL ,
    status_enum VARCHAR(50) NOT NULL DEFAULT 'PENDING',

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_cart_id (cart_id),
    INDEX idx_customer_id (customer_id),
    INDEX idx_delivery_address_id(delivery_address_id),
    INDEX idx_store_keeper_id (store_keeper_id),
    INDEX idx_status_enum (status_enum)
);





