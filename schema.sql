
CREATE TABLE IF NOT EXISTS item (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(60) NOT NULL UNIQUE,
    unit VARCHAR(10) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_item_created_at (created_at),
    INDEX idx_item_updated_at (updated_at)
);



CREATE TABLE IF NOT EXISTS image (
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
    id BIGINT PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    email_id VARCHAR(50) NOT NULL UNIQUE,
    dob VARCHAR(15) NOT NULL,
    mobile_number VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_customer_email (email_id)
);



CREATE TABLE IF NOT EXISTS address (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    address_line1 VARCHAR(100),
    address_line2 VARCHAR(100),
    landmark VARCHAR(20),
    city VARCHAR(30),
    state VARCHAR(30),
    pincode VARCHAR(6),
    customer_id BIGINT,
    is_default BOOLEAN NOT NULL DEFAULT FALSE,

    INDEX idx_address_customer_id (customer_id),
    INDEX idx_address_city (city),
    INDEX idx_address_pincode (pincode),
    INDEX idx_address_customer_default (customer_id, is_default)
);

CREATE TABLE IF NOT EXISTS storekeeper (
    id BIGINT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    store_name VARCHAR(30) NOT NULL,
    mobile_number VARCHAR(20) NOT NULL UNIQUE,
    gst_in VARCHAR(30) NOT NULL UNIQUE,
    address_line1 VARCHAR(100) NOT NULL UNIQUE,
    address_line2 VARCHAR(100),
    landmark VARCHAR(20),
    city VARCHAR(30) NOT NULL,
    state VARCHAR(30) NOT NULL,
    pincode VARCHAR(6) NOT NULL,
    store_id VARCHAR(20) UNIQUE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


 CREATE TABLE customer_storekeeper(
 customer_id BIGINT NOT NULL,
 store_id VARCHAR(20) NOT NULL,
 PRIMARY KEY (customer_id, store_id),
 FOREIGN KEY (customer_id) REFERENCES customer(id),
 FOREIGN KEY (store_id) REFERENCES storekeeper(store_id)
 );


 CREATE TABLE IF NOT EXISTS cart (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    customer_id BIGINT NOT NULL UNIQUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_cart_customer FOREIGN KEY (customer_id) REFERENCES customer(id),

    UNIQUE INDEX idx_cart_customer_id (customer_id)
);


CREATE TABLE IF NOT EXISTS cart_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    cart_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    item_id INT NOT NULL,

    quantity INT NOT NULL,
    unit VARCHAR(10),

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_cart_item_cart FOREIGN KEY (cart_id) REFERENCES cart(id),
    CONSTRAINT fk_cart_item_customer FOREIGN KEY (customer_id) REFERENCES customer(id),
    CONSTRAINT fk_cart_item_item FOREIGN KEY (item_id) REFERENCES item(id)
);



