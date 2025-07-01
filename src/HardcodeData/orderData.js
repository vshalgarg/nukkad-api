const orderData = [
  {
    orderId: "ORD123456",
    trackingNumber: "TRK789012",
    customerName: "John",
    landmark: "Near XYZ School",
    date: "2025-06-01",
    quantity: 3,
    status: "Pending",
    mobileNumber: "8171845998",
    address: "newDelhi,110001",
    items: [
      {
        id: "g001",
        title: "Fresh Bananas",
        image:
          "https://th.bing.com/th/id/R.eced5a898beb20a6931561f100bb97ce?rik=s6lRofFQPg%2f7og&riu=http%3a%2f%2fpngimg.com%2fuploads%2fbanana%2fbanana_PNG814.png&ehk=lxcXNOb0Urkc71g2CmtbUwWN4PzmqV4as%2fFaITE7Cvg%3d&risl=1&pid=ImgRaw&r=0",
        category: "Fruits",
        weight: "5kg",
        deals: "5% OFF",
        stock: 50,
      },
      {
        id: "g002",
        title: "Organic Tomatoes",
        image:
          "https://th.bing.com/th/id/OIP.K0g6PLL9h3fPFaoIBpfZFwHaE8?rs=1&pid=ImgDetMain",
        category: "Vegetables",
        weight: "4kg",
        deals: null,
        stock: 30,
      },
      {
        id: "g003",
        title: "Whole Milk",
        image:
          "https://th.bing.com/th/id/OIP.9LZU9dm7WCz7Du4EoSvyiwHaHa?rs=1&pid=ImgDetMain",
        category: "Dairy",
        weight: "1l",
        deals: "10% OFF",
        stock: 25,
      },
      {
        id: "g005",
        title: "Fresh Bananas",
        image:
          "https://th.bing.com/th/id/R.eced5a898beb20a6931561f100bb97ce?rik=s6lRofFQPg%2f7og&riu=http%3a%2f%2fpngimg.com%2fuploads%2fbanana%2fbanana_PNG814.png&ehk=lxcXNOb0Urkc71g2CmtbUwWN4PzmqV4as%2fFaITE7Cvg%3d&risl=1&pid=ImgRaw&r=0",
        category: "Fruits",
        weight: "1kg",
        deals: "5% OFF",
        stock: 50,
      },
      {
        id: "g004",
        title: "Brown Bread",
        image:
          "https://th.bing.com/th/id/OIP.4vVrr3ScuPURq_150LH4IwHaHa?rs=1&pid=ImgDetMain",
        category: "Bakery",
        weight: "400g",
        deals: null,
        stock: 50, // Out of stock example
      },
      {
        id: "g006",
        title: "Organic Tomatoes",
        image:
          "https://th.bing.com/th/id/OIP.K0g6PLL9h3fPFaoIBpfZFwHaE8?rs=1&pid=ImgDetMain",
        category: "Vegetables",
        weight: "1kg",
        deals: null,
        stock: 30,
      },
      {
        id: "g008",
        title: "Brown Bread",
        image:
          "https://th.bing.com/th/id/OIP.4vVrr3ScuPURq_150LH4IwHaHa?rs=1&pid=ImgDetMain",
        category: "Bakery",
        weight: "400g",
        deals: null,
        stock: 30, // Out of stock example
      },
      {
        id: "g007",
        title: "Whole Milk",
        image:
          "https://th.bing.com/th/id/OIP.9LZU9dm7WCz7Du4EoSvyiwHaHa?rs=1&pid=ImgDetMain",
        category: "Dairy",
        weight: "1l",
        deals: "10% OFF",
        stock: 25,
      },
    ],
  },
  {
    orderId: "ORD124356",
    trackingNumber: "TRK789013",
    customerName: "Marco",
    landmark: "Near XYZ Park",
    mobileNumber: "9985771849",
    date: "2025-04-03",
    quantity: 3,
    status: "Pending",
    address: "newDelhi,110001",

    items: [
      {
        productId: "g007",
        title: "Whole Milk",
        image:
          "https://th.bing.com/th/id/OIP.9LZU9dm7WCz7Du4EoSvyiwHaHa?rs=1&pid=ImgDetMain",
        category: "Dairy",
        weight: "1l",
        deals: "10% OFF",
        stock: 25,
      },
    ],
  },
  {
    orderId: "ORD345678",
    trackingNumber: "TRK789014",
    customerName: "Rishab",
    mobileNumber: "8195775849",
    landmark: "Near XYZ Hospital",
    date: "2025-06-02",
    quantity: 1,
    status: "Pending",
    address: "newDelhi,110001",

    items: [
      {
        id: "g008",
        title: "Brown Bread",
        image:
          "https://th.bing.com/th/id/OIP.4vVrr3ScuPURq_150LH4IwHaHa?rs=1&pid=ImgDetMain",
        category: "Bakery",
        weight: "400g",
        deals: null,
        stock: 30,
      },
      {
        id: "g007",
        title: "Whole Milk",
        image:
          "https://th.bing.com/th/id/OIP.9LZU9dm7WCz7Du4EoSvyiwHaHa?rs=1&pid=ImgDetMain",
        category: "Dairy",
        weight: "1l",
        deals: "10% OFF",
        stock: 25,
      },
    ],
  },
  {
    orderId: "ORD654321",
    trackingNumber: "TRK456789",
    customerName: "Jacob",
    mobileNumber: "8176872709",
    landmark: "Near XYZ Police",
    date: "2025-05-25",
    quantity: 2,
    status: "Pending",
    address: "newDelhi,110001",
    items: [
      {
        id: "g006",
        title: "Organic Tomatoes",
        image:
          "https://th.bing.com/th/id/OIP.K0g6PLL9h3fPFaoIBpfZFwHaE8?rs=1&pid=ImgDetMain",
        category: "Vegetables",
        weight: "1kg",
        deals: null,
        stock: 30,
      },
      {
        id: "g007",
        title: "Whole Milk",
        image:
          "https://th.bing.com/th/id/OIP.9LZU9dm7WCz7Du4EoSvyiwHaHa?rs=1&pid=ImgDetMain",
        category: "Dairy",
        weight: "1l",
        deals: "10% OFF",
        stock: 25,
      },
    ],
  },
  {
    orderId: "ORD789012",
    trackingNumber: "TRK789015",
    date: "2025-05-20",
    mobileNumber: "7069548249",
    customerName: "Rohan",
    landmark: "Near Metro",
    quantity: 2,
    status: "Pending",
    address: "newDelhi,110001",
    items: [
      {
        id: "g008",
        title: "Brown Bread",
        image:
          "https://th.bing.com/th/id/OIP.4vVrr3ScuPURq_150LH4IwHaHa?rs=1&pid=ImgDetMain",
        category: "Bakery",
        weight: "400g",
        deals: null,
        stock: 30,
      },
      {
        id: "g006",
        title: "Organic Tomatoes",
        image:
          "https://th.bing.com/th/id/OIP.K0g6PLL9h3fPFaoIBpfZFwHaE8?rs=1&pid=ImgDetMain",
        category: "Vegetables",
        weight: "1kg",
        deals: null,
        stock: 30,
      },
    ],
  },
  {
    orderId: "ORD888888",
    trackingNumber: "TRK789016",
    customerName: "Sonia",
    mobileNumber: "8059357496",
    landmark: "Near Bus Station",
    date: "2025-04-15",
    quantity: 1,
    status: "Pending",
    address: "newDelhi,110001",
    items: [
      {
        id: "g008",
        title: "Brown Bread",
        image:
          "https://th.bing.com/th/id/OIP.4vVrr3ScuPURq_150LH4IwHaHa?rs=1&pid=ImgDetMain",
        category: "Bakery",
        weight: "400g",
        deals: null,
        stock: 30,
      },
    ],
  },

  {
    orderId: "ORD186876",
    trackingNumber: "TRK789012",
    customerName: "Danny",
    landmark: "Near XYZ School",
    date: "2025-06-01",
    quantity: 3,
    status: "Pending",
    mobileNumber: "9581839548",
    address: "newDelhi,110001",
    items: [
      {
        id: "g001",
        title: "Fresh Bananas",
        image:
          "https://th.bing.com/th/id/R.eced5a898beb20a6931561f100bb97ce?rik=s6lRofFQPg%2f7og&riu=http%3a%2f%2fpngimg.com%2fuploads%2fbanana%2fbanana_PNG814.png&ehk=lxcXNOb0Urkc71g2CmtbUwWN4PzmqV4as%2fFaITE7Cvg%3d&risl=1&pid=ImgRaw&r=0",
        category: "Fruits",
        weight: "5kg",
        deals: "5% OFF",
        stock: 50,
      },
      {
        id: "g002",
        title: "Organic Tomatoes",
        image:
          "https://th.bing.com/th/id/OIP.K0g6PLL9h3fPFaoIBpfZFwHaE8?rs=1&pid=ImgDetMain",
        category: "Vegetables",
        weight: "4kg",
        deals: null,
        stock: 30,
      },
      {
        id: "g003",
        title: "Whole Milk",
        image:
          "https://th.bing.com/th/id/OIP.9LZU9dm7WCz7Du4EoSvyiwHaHa?rs=1&pid=ImgDetMain",
        category: "Dairy",
        weight: "1l",
        deals: "10% OFF",
        stock: 25,
      },
      {
        id: "g005",
        title: "Fresh Bananas",
        image:
          "https://th.bing.com/th/id/R.eced5a898beb20a6931561f100bb97ce?rik=s6lRofFQPg%2f7og&riu=http%3a%2f%2fpngimg.com%2fuploads%2fbanana%2fbanana_PNG814.png&ehk=lxcXNOb0Urkc71g2CmtbUwWN4PzmqV4as%2fFaITE7Cvg%3d&risl=1&pid=ImgRaw&r=0",
        category: "Fruits",
        weight: "1kg",
        deals: "5% OFF",
        stock: 50,
      },
      {
        id: "g004",
        title: "Brown Bread",
        image:
          "https://th.bing.com/th/id/OIP.4vVrr3ScuPURq_150LH4IwHaHa?rs=1&pid=ImgDetMain",
        category: "Bakery",
        weight: "400g",
        deals: null,
        stock: 50,
      },
    ],
  },
];

export default orderData;
