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
    CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customer(id),
    CONSTRAINT fk_storekeeper FOREIGN KEY (storekeeper_id) REFERENCES storekeeper(id)
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

CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    cart_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    delivery_address_id BIGINT NOT NULL,
    store_keeper_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,

    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- Foreign Key Constraints
    CONSTRAINT fk_order_cart FOREIGN KEY (cart_id) REFERENCES cart_item(id),
    CONSTRAINT fk_order_customer FOREIGN KEY (customer_id) REFERENCES customer(id),
    CONSTRAINT fk_order_delivery FOREIGN KEY (delivery_address_id) REFERENCES address(id),
    CONSTRAINT fk_order_store_keeper FOREIGN KEY (store_keeper_id) REFERENCES storekeeper(id),

    -- Indexes for Faster Lookups
    INDEX idx_order_cart (cart_id),
    INDEX idx_order_customer (customer_id),
    INDEX idx_order_delivery (delivery_address_id),
    INDEX idx_order_store_keeper (store_keeper_id),
    INDEX idx_order_status (status)
);
CREATE TABLE IF NOT EXISTS cartitem_Order (
    order_id BIGINT NOT NULL,
    cart_item_id BIGINT NOT NULL,

    PRIMARY KEY (order_id, cart_item_id),

    CONSTRAINT fk_ic_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_ic_cart_item FOREIGN KEY (cart_item_id) REFERENCES cart_item(id) ON DELETE CASCADE,

    INDEX idx_ic_cart_item_id (cart_item_id)
);

CREATE TABLE rating (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    review VARCHAR(255) NOT NULL,
    rating INT NOT NULL,

    customer_id BIGINT NOT NULL,
    storekeeper_id BIGINT NOT NULL,

     created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
        updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_rating_customer FOREIGN KEY (customer_id) REFERENCES customer(id),
    CONSTRAINT fk_rating_storekeeper FOREIGN KEY (storekeeper_id) REFERENCES storekeeper(id),

    INDEX idx_rating_customer(customer_id),
    INDEX idx_rating_storekeeper(storekeeper_id)
);