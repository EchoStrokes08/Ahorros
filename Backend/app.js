import express from 'express'

import {
  usuarios,
  metas,
  pagos
} from './data.js'

const app = express()
const port = 3000

/*
=====================================
usuarios
=====================================
*/

app.get('/usuarios', (req, res) => {
  res.send(usuarios)
})

app.get(
  '/usuarios/dispositivo/:dispositivoId',

  (req, res) => {

    const dispositivoId =
      req.params.dispositivoId

    const usuario = usuarios.find(
      u =>
        u.dispositivoId ===
        dispositivoId
    )

    if (!usuario) {

      return res.status(404).send({
        error: 'Usuario no encontrado'
      })

    }

    res.send(usuario)

  }
)

app.post('/usuarios', (req, res) => {

  const NuevoUsuario = {
    id: usuarios.length + 1,
    nombre: req.body.name,
    idDispositivo: req.body.IdDispositivo,
    amigos: []
  }

  usuarios.push(NuevoUsuario)

  res.status(201).send(NuevoUsuario)
})

app.put('/usuarios/:id', (req, res) => {

  const id = parseInt(req.params.id)

  const usuario = usuarios.find(u => u.id === id)

  if (!usuario) {
    return res.status(404).send({
      error: 'Usuario no encontrado'
    })
  }

  usuario.nombre = req.body.name

  res.send(usuario)
})

/*
=====================================
agregar amigo
=====================================
*/

app.post('/usuarios/:id/amigos', (req, res) => {

  const id = parseInt(req.params.id)

  const IdAmigo = req.body.friendId

  const usuario = usuarios.find(u => u.id === id)

  const amigo = usuarios.find(u => u.id === IdAmigo)

  if (!usuario || !amigo) {
    return res.status(404).send({
      error: 'Usuario no encontrado'
    })
  }
  if (!usuario.amigos.includes(IdAmigo)) {
    usuario.amigos.push(IdAmigo)
  }

  if (!amigo.amigos.includes(id)) {
    amigo.amigos.push(id)
  }

  res.send({
    message: 'Amigo agregado'
  })
})

/*
=====================================
metas
=====================================
*/

/*
Solo devuelve metas donde:
- el usuario es dueño
- o es miembro
*/

app.get('/usuarios/:id/metas', (req, res) => {

  const IdUsuario = parseInt(req.params.id)

  const metasVisiles = metas.filter(meta => {

    return (
      meta.IdPrincipal === IdUsuario ||
      meta.miembros.includes(IdUsuario)
    )
  })

  res.send(metasVisiles)
})

/*
Crear meta
*/

app.post('/metas', (req, res) => {

  const nuevaMeta = {
    id: metas.length + 1,
    titulo: req.body.title,
    montoObjetivo: req.body.targetAmount,
    imagen: req.body.image,
    IdPrincipal: req.body.ownerId,
    miembros: [req.body.ownerId]
  }

  metas.push(nuevaMeta)

  res.status(201).send(nuevaMeta)
})

/*
Detalle meta
*/

app.get('/metas/:metaId', (req, res) => {

  const metaId = parseInt(req.params.metaId)

  const IdUsuario = parseInt(req.query.userId)

  const meta = metas.find(m => m.id === metaId)

  if (!meta) {
    return res.status(404).send({
      error: 'Meta no encontrada'
    })
  }

  /*
  Validar acceso
  */

  const hasAccess =
    meta.IdPrincipal === IdUsuario ||
    meta.miembros.includes(IdUsuario)

  if (!hasAccess) {
    return res.status(403).send({
      error: 'No tienes acceso a esta meta'
    })
  }

  const pagosDeLaMeta = pagos.filter(
    p => p.metaId === metaId
  )

  const totalSalvado = pagosDeLaMeta.reduce(
    (sum, pago) => sum + pago.amount,
    0
  )

  const porcentaje =
    (totalSalvado / meta.montoObjetivo) * 100

  res.send({
    ...meta,
    pagosDeLaMeta,
    totalSalvado,
    porcentaje
  })
})

/*
Agregar miembro a meta
*/

app.post('/metas/:idMeta/miembros', (req, res) => {

  const idMeta = parseInt(req.params.idMeta)

  const idUsuario = req.body.idUsuario

  const idSolicitante = req.body.idSolicitante

  const meta = metas.find(
    m => m.id === idMeta
  )

  // ===============================
  // validar meta
  // ===============================

  if (!meta) {

    return res.status(404).send({
      error: 'Meta no encontrada'
    })

  }

  // ===============================
  // solo el dueño puede agregar miembros
  // ===============================

  if (meta.idPrincipal !== idSolicitante) {

    return res.status(403).send({
      error: 'Solo el dueño puede agregar miembros'
    })

  }

  // ===============================
  // validar amistad
  // ===============================

  const dueño = usuarios.find(
    u => u.id === meta.idPrincipal
  )

  if (!dueño.amigos.includes(idUsuario)) {

    return res.status(400).send({
      error: 'Solo puedes agregar amigos'
    })

  }

  // ===============================
  // agregar miembro
  // ===============================

  if (!meta.miembros.includes(idUsuario)) {

    meta.miembros.push(idUsuario)

  }

  // ===============================
  // respuesta
  // ===============================

  res.send({
    mensaje: 'Miembro agregado'
  })

})

/*
=====================================
pagos
=====================================
*/

app.post('/pagos', (req, res) => {

  const meta = metas.find(
    m => m.id === req.body.metaId
  )

  if (!meta) {
    return res.status(404).send({
      error: 'Meta no encontrada'
    })
  }

  /*
  Solo miembros pueden pagar
  */

  const esMiembro =
    meta.miembros.includes(req.body.userId)

  if (!esMiembro) {
    return res.status(403).send({
      error: 'No perteneces a esta meta'
    })
  }

  const nuevoPago = {
    id: pagos.length + 1,
    metaId: req.body.metaId,
    userId: req.body.userId,
    amount: req.body.amount,
    date: new Date().toISOString()
  }

  pagos.push(nuevoPago)

  res.status(201).send(nuevoPago)
})

/*
Consultar pagos
*/

app.get('/metas/:metaId/pagos', (req, res) => {

  const metaId = parseInt(req.params.metaId)

  const pagosdeLaMeta = pagos.filter(
    p => p.metaId === metaId
  )

  res.send(pagosdeLaMeta)
})

app.listen(port, () => {
  console.log(`Running on port ${port}`)
})