import { calcularDescuento } from './step2-discount';

describe('calcularDescuento', () => {
  it('calculates preferente discount', () => {
    expect(calcularDescuento(100, 'preferente')).toBe(90);
  });

  it('calculates vip discount', () => {
    expect(calcularDescuento(200, 'vip')).toBe(160);
  });

  it('no discount for others', () => {
    expect(calcularDescuento(50, 'normal')).toBe(50);
  });
});