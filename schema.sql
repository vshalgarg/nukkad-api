
CREATE TABLE IF NOT EXISTS item (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(60) NOT NULL UNIQUE ,
    unit TINYINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,


    INDEX idx_item_name (name),
    INDEX idx_item_created_at (created_at),
    INDEX idx_item_updated_at (updated_at)
);

CREATE TABLE IF NOT EXISTS category_item_image (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    image_url VARCHAR(200) NOT NULL,
    item_id INT,


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

    CONSTRAINT fk_category_image FOREIGN KEY (image_id) REFERENCES category_item_image(id),

    INDEX idx_category_name (name)
);



CREATE TABLE IF NOT EXISTS customer (
    id BIGINT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email_id VARCHAR(50) NOT NULL UNIQUE,
    dob VARCHAR(15) NOT NULL,
    mobile_number VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_customer_mobile (mobile_number),
    INDEX idx_customer_name_dob (name, dob),
    INDEX idx_customer_created_at (created_at)
);

CREATE TABLE IF NOT EXISTS storekeeper (
    id BIGINT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    store_name VARCHAR(30) NOT NULL,
    mobile_number VARCHAR(20) NOT NULL UNIQUE,
    contact_number VARCHAR(20),
    gst_in VARCHAR(30) NOT NULL UNIQUE,
    address_line1 VARCHAR(100) NOT NULL UNIQUE,
    address_line2 VARCHAR(100),
    landmark VARCHAR(100),
    city VARCHAR(30) NOT NULL,
    state VARCHAR(30) NOT NULL,
    pincode VARCHAR(6) NOT NULL,
    store_qr_id VARCHAR(20) UNIQUE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_storekeeper_store_name (store_name),
    INDEX idx_storekeeper_created_at (created_at)
);

CREATE TABLE IF NOT EXISTS storekeeper_image (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    image_url VARCHAR(200) NOT NULL,
    storekeeper_id BIGINT NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_storekeeper_image FOREIGN KEY (storekeeper_id) REFERENCES storekeeper(id) ON DELETE CASCADE,
    INDEX idx_storekeeper_image_storekeeper_id (storekeeper_id)
);

CREATE TABLE IF NOT EXISTS storekeeper_qr_code (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    qr_image_url VARCHAR(200) NOT NULL,
    is_default BOOLEAN NOT NULL DEFAULT FALSE,
    storekeeper_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_storekeeper_qr_code_storekeeper FOREIGN KEY (storekeeper_id) REFERENCES storekeeper(id) ON DELETE CASCADE,
    INDEX idx_storekeeper_qr_code_storekeeper_id (storekeeper_id)
);

CREATE TABLE IF NOT EXISTS item_category (
    item_id INT NOT NULL,
    category_id INT NOT NULL,

    CONSTRAINT uq_item_category UNIQUE (item_id, category_id),

    CONSTRAINT fk_ic_item FOREIGN KEY (item_id) REFERENCES item(id) ON DELETE CASCADE,
    CONSTRAINT fk_ic_category FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE,

    INDEX idx_ci_category_item (category_id, item_id)
);

CREATE TABLE IF NOT EXISTS customer_storekeeper (
    customer_id BIGINT NOT NULL,
    storekeeper_id BIGINT NOT NULL,

    CONSTRAINT uq_customer_storekeeper UNIQUE (customer_id, storekeeper_id),

    CONSTRAINT fk_cs_customer FOREIGN KEY (customer_id) REFERENCES customer(id),
    CONSTRAINT fk_cs_storekeeper FOREIGN KEY (storekeeper_id) REFERENCES storekeeper(id),

    INDEX idx_cs_store_customer (storekeeper_id, customer_id)
);

CREATE TABLE IF NOT EXISTS address (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    name VARCHAR(50),
    mobile_number VARCHAR(10),

    address_line1 VARCHAR(100) NOT NULL,
    address_line2 VARCHAR(100),
    landmark VARCHAR(100) NOT NULL,
    city VARCHAR(30) NOT NULL,
    state VARCHAR(30) NOT NULL,
    pincode VARCHAR(6) NOT NULL,
    customer_id BIGINT,
    is_default BOOLEAN NOT NULL DEFAULT FALSE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_address_customer_id (customer_id),
    INDEX idx_address_city (city),
    INDEX idx_address_pincode (pincode),
    INDEX idx_address_customer_default (customer_id, is_default)
);



  CREATE TABLE IF NOT EXISTS cart (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,

     customer_id BIGINT NOT NULL UNIQUE,

     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

     CONSTRAINT fk_cart_customer FOREIGN KEY (customer_id) REFERENCES customer(id),

     INDEX idx_cart_customer_id (customer_id)
 );

CREATE TABLE IF NOT EXISTS cart_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cart_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    item_id INT NOT NULL,
    quantity INT NOT NULL,
    unit VARCHAR(10),

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_cart_item_cart FOREIGN KEY (cart_id) REFERENCES cart(id),
    CONSTRAINT fk_cart_item_customer FOREIGN KEY (customer_id) REFERENCES customer(id),
    CONSTRAINT fk_cart_item_item FOREIGN KEY (item_id) REFERENCES item(id),

    INDEX idx_cart_item_cart_id (cart_id),
    INDEX idx_cart_item_customer_id (customer_id),
    INDEX idx_cart_item_cart_item (cart_id, item_id)
);


CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    customer_id BIGINT NOT NULL,
    delivery_address_id BIGINT NOT NULL,
    store_keeper_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    store_keeper_note VARCHAR(255),


    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_order_customer FOREIGN KEY (customer_id) REFERENCES customer(id),
    CONSTRAINT fk_order_delivery FOREIGN KEY (delivery_address_id) REFERENCES address(id),
    CONSTRAINT fk_order_store_keeper FOREIGN KEY (store_keeper_id) REFERENCES storekeeper(id),

    INDEX idx_order_customer (customer_id),
    INDEX idx_order_delivery (delivery_address_id),
    INDEX idx_order_store_keeper (store_keeper_id),
    INDEX idx_order_status (status)
);



CREATE TABLE IF NOT EXISTS order_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    order_id BIGINT NOT NULL,
    item_id INT NOT NULL,
    item_name VARCHAR(50),
    quantity INT NOT NULL,
    unit VARCHAR(50),
    price DOUBLE,

    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_order_item_item FOREIGN KEY (item_id) REFERENCES item(id) ON DELETE CASCADE,

    INDEX idx_order_item_order_id (order_id),
    INDEX idx_order_item_item_id (item_id)
);

CREATE TABLE  IF NOT EXISTS  rating (
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

CREATE TABLE user_device_token (
    id INT AUTO_INCREMENT PRIMARY KEY,
    device_token VARCHAR(255),

    customer_id BIGINT unique,
    storekeeper_id BIGINT unique,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

