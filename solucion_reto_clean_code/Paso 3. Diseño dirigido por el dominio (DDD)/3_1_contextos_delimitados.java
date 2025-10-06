/*
1. Contexto de Catálogo
Responsabilidad: Gestión de productos y disponibilidad
*/
// Entidades principales
Producto
- id, nombre, precio, stock
- actualizarStock()

// Servicios del contexto
ServicioCatalogo
- buscarProductos()
- verificarDisponibilidad()
- actualizarPrecios()

/*
2. Contexto de Ventas (Carrito/Pedido)
Responsabilidad: Gestión del proceso de compra desde selección hasta orden
*/
// Entidades principales
Carrito
- idCliente, items
- agregarItem(), calcularTotal()

ItemCarrito
- producto, cantidad

Pedido
- id, estado, total
- confirmar(), cancelar()

// Servicios del contexto
ServicioVentas
- crearPedidoDesdeCarrito()
- calcularTotales()
- gestionarEstados()

/*
3. Contexto de Pagos
Responsabilidad: Procesamiento y gestión de transacciones financieras
*/
// Entidades principales
Pago
- idPedido, monto, medioPago
- procesar()

// Servicios del contexto  
ServicioPagos
- procesarPago()
- verificarEstadoTransaccion()
- gestionarReembolsos()

// Value Objects
MedioPago (TARJETA, PAYPAL, TRANSFERENCIA)
EstadoPago (PENDIENTE, EXITOSO, FALLIDO, REEMBOLSADO)