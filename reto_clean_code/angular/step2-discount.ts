// Función para calcular descuentos según el tipo de cliente.  Se utiliza en
// el paso 2 para practicar TDD y refactorización.

export function calcularDescuento(precio: number, tipoCliente: string): number {
  const descuentos: { [key: string]: number } = {
    preferente: 0.1,
    vip: 0.2
  };
  return precio * (1 - (descuentos[tipoCliente] || 0));
}