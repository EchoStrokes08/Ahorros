export const usuarios = [
  {
    id: 1,
    nombre: 'Jonathan',
    idDispositivo: '1234567890',
    amigos: [2]
  },
  {
    id: 2,
    nombre: 'Danna',
    idDispositivo: '0987654321',
    amigos: [1]
  }
]

export const metas = [
  {
    id: 1,
    titulo: 'Moto',
    montoObjetivo: 12000000,
    imagen: 'https://...',
    IdPrincipal: 1,
    miembros: [1, 2]
  }
]

export const pagos = [
  {
    id: 1,
    metaId: 1,
    IdMiembro: 2,
    montoAportado: 200000,
    fecha: '2026-05-22'
  }
]