CREATE TABLE IF NOT EXISTS item (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(60) NOT NULL UNIQUE ,
    unit TINYINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,


    INDEX idx_item_name (name),
    INDEX idx_item_unit (unit),
    INDEX idx_item_created_at (created_at),
    INDEX idx_item_updated_at (updated_at)
);


CREATE TABLE IF NOT EXISTS image (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    image_url VARCHAR(200) NOT NULL,
    item_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,


    CONSTRAINT fk_image_item FOREIGN KEY (item_id) REFERENCES item(id) ON DELETE CASCADE,


    INDEX idx_image_item_id (item_id)
);


CREATE TABLE IF NOT EXISTS category (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    image_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,


    CONSTRAINT fk_category_image FOREIGN KEY (image_id) REFERENCES image(id),


    INDEX idx_category_image_id (image_id),
    INDEX idx_category_created_at (created_at),
    INDEX idx_category_updated_at (updated_at)
);

CREATE TABLE IF NOT EXISTS item_category (
    item_id INT NOT NULL,
    category_id INT NOT NULL,

    PRIMARY KEY (item_id, category_id),


    CONSTRAINT fk_ic_item FOREIGN KEY (item_id) REFERENCES item(id) ON DELETE CASCADE,
    CONSTRAINT fk_ic_category FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE,


    INDEX idx_ic_category_id (category_id)
);

CREATE TABLE IF NOT EXISTS customer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email_id VARCHAR(50) NOT NULL UNIQUE,
    address_Line1 VARCHAR(200) NOT NULL,
    address_Line2 VARCHAR(200),
    city VARCHAR(30) NOT NULL,
    state VARCHAR(30) NOT NULL,
    pincode VARCHAR(6) NOT NULL,
    dob VARCHAR(15) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_customer_email (email_id),
    INDEX idx_customer_city (city),
    INDEX idx_customer_state (state)
);


CREATE TABLE IF NOT EXISTS  address (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    label VARCHAR(10),
    address_line1 VARCHAR(200) NOT NULL,
    address_line2 VARCHAR(200),
    landmark VARCHAR(50) NOT NULL,
    city VARCHAR(30) NOT NULL,
    state VARCHAR(30) NOT NULL,
    pincode VARCHAR(6) NOT NULL,
    customer_id BIGINT NOT NULL,
    storekeeper_id BIGINT NOT NULL,
    created_at TIMESTAMP,         -- Assuming BaseEntity has createdAt, updatedAt fields
    updated_at TIMESTAMP,
    CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customers(id),
    CONSTRAINT fk_storekeeper FOREIGN KEY (storekeeper_id) REFERENCES storekeepers(id)
);

--
CREATE TABLE IF NOT EXISTS storekeeper (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    store_name VARCHAR(30) NOT NULL,
    contact_number VARCHAR(20) NOT NULL UNIQUE,
    gst_in VARCHAR(30) NOT NULL UNIQUE,
    address_line1 VARCHAR(200) NOT NULL UNIQUE,
    address_line2 VARCHAR(200) NOT NULL UNIQUE,
    city VARCHAR(30) NOT NULL,
    state VARCHAR(30) NOT NULL,
    pincode VARCHAR(6) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS cart_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    item_id INT NOT NULL,
    quantity INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_cart_item_item FOREIGN KEY (item_id) REFERENCES item (id)
);

CREATE TABLE order_entity (
    id INT AUTO_INCREMENT PRIMARY KEY,

    cart_id INT NOT NULL,
    customer_id BIGINT NOT NULL ,
    delivery_address BIGINT NOT NULL ,
    store_keeper_id BIGINT NOT NULL ,
    status_enum VARCHAR(50) NOT NULL ,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- Foreign keys (update table names as needed)
    CONSTRAINT fk_order_customer FOREIGN KEY (customer_id) REFERENCES customer(id),
    CONSTRAINT fk_order_storekeeper FOREIGN KEY (store_keeper_id) REFERENCES storekeeper(id),

    -- Indexes
    INDEX idx_cart_id (cart_id),
    INDEX idx_customer_id (customer_id),
    INDEX idx_delivery_address (delivery_address),
    INDEX idx_store_keeper_id (store_keeper_id),
    INDEX idx_status_enum (status_enum)
);
CREATE TABLE my_order_entity (
    id INT PRIMARY KEY AUTO_INCREMENT,

    customer_id BIGINT NOT NULL,
    order_id INT NOT NULL,
    status_enum VARCHAR(50) NOT NULL,
    store_keeper_id BIGINT NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- Foreign key constraints
    CONSTRAINT fk_myorder_customer FOREIGN KEY (customer_id) REFERENCES customer(id),
    CONSTRAINT fk_myorder_order FOREIGN KEY (order_id) REFERENCES order_entity(id),
    CONSTRAINT fk_myorder_storekeeper FOREIGN KEY (store_keeper_id) REFERENCES storekeeper(id)
);



