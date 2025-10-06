"""
Pruebas unitarias para la función `calcular_descuento`.  Se pueden ejecutar
utilizando pytest o unittest.  Amplía las pruebas según sea necesario.
"""

import pytest
from step2_discount import calcular_descuento


def test_preferente():
    assert calcular_descuento(100, 'preferente') == 90


def test_vip():
    assert calcular_descuento(200, 'vip') == 160


def test_otro():
    assert calcular_descuento(50, 'normal') == 50