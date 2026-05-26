import express from 'express'
import fs from 'fs'

/*
=====================================
IMPORTAR DATOS
=====================================

Se importan los arreglos que funcionan
como una base de datos temporal.

- usuarios
- metas
- pagos
*/

import {
  usuarios,
  metas,
  pagos
} from './data.js'

/*
=====================================
CONFIGURACIÓN INICIAL
=====================================
*/

const app = express()
const port = 3000

/*
Permite recibir JSON desde Retrofit
o cualquier cliente HTTP.
*/

app.use(express.json())


/*
=====================================
GUARDAR ARCHIVO data.js
=====================================
*/

function guardarData() {

  const contenido = `

export const usuarios = ${JSON.stringify(
    usuarios,
    null,
    2
  )}

export const metas = ${JSON.stringify(
    metas,
    null,
    2
  )}

export const pagos = ${JSON.stringify(
    pagos,
    null,
    2
  )}

`

  fs.writeFileSync(
    './data.js',
    contenido
  )

}

/*
#####################################################
USUARIOS
#####################################################
*/

/*
=====================================
OBTENER TODOS LOS USUARIOS
=====================================

GET /usuarios
*/

app.get('/usuarios', (req, res) => {

  res.send(usuarios)

})

/*
=====================================
BUSCAR USUARIO POR ID DEL DISPOSITIVO
=====================================

GET /usuarios/dispositivo/:idDispositivo

Ejemplo:
GET /usuarios/dispositivo/12345

Se usa para identificar al usuario
desde el celular sin login.
*/

app.get(
  '/usuarios/dispositivo/:idDispositivo',

  (req, res) => {

    const dispositivoId =
      req.params.idDispositivo

    /*
    Buscar usuario
    */

    const usuario = usuarios.find(

      u =>
        u.idDispositivo ===
        dispositivoId

    )

    /*
    Validar existencia
    */

    if (!usuario) {

      return res.status(404).send({
        error: 'Usuario no encontrado'
      })

    }

    /*
    Respuesta
    */

    res.send(usuario)

  }
)

/*
=====================================
CREAR USUARIO
=====================================

POST /usuarios

Body:
{
  "nombre": "Jonathan",
  "idDispositivo": "12345"
}
*/

app.post('/usuarios', (req, res) => {

  /*
  Crear objeto usuario
  */

  const nuevoUsuario = {

    id: usuarios.length + 1,

    nombre: req.body.nombre,

    idDispositivo:
      req.body.idDispositivo,

    amigos: []

  }

  /*
  Guardar usuario
  */

  usuarios.push(nuevoUsuario)

  /*
  Respuesta
  */

  guardarData()

  res.status(201).send(
    nuevoUsuario
  )

})

/*
=====================================
EDITAR USUARIO
=====================================

PUT /usuarios/:id

Body:
{
  "nombre": "Nuevo nombre"
}
*/

app.put('/usuarios/:id', (req, res) => {

  /*
  Obtener id
  */

  const id =
    parseInt(req.params.id)

  /*
  Buscar usuario
  */

  const usuario = usuarios.find(
    u => u.id === id
  )

  /*
  Validar existencia
  */

  if (!usuario) {

    return res.status(404).send({
      error: 'Usuario no encontrado'
    })

  }

  /*
  Actualizar nombre
  */

  usuario.nombre =
    req.body.nombre

  /*
  Respuesta
  */
  guardarData()
  res.send(usuario)

})

/*
#####################################################
AMIGOS
#####################################################
*/

/*
=====================================
AGREGAR AMIGO
=====================================

POST /usuarios/:id/amigos

Body:
{
  "friendId": 2
}

La amistad se agrega en ambos lados.
*/

app.post(
  '/usuarios/:id/amigos',

  (req, res) => {

    /*
    Usuario principal
    */

    const id =
      parseInt(req.params.id)

    /*
    Usuario amigo
    */

    const idAmigo =
      req.body.friendId

    /*
    Buscar usuarios
    */

    const usuario = usuarios.find(
      u => u.id === id
    )

    const amigo = usuarios.find(
      u => u.id === idAmigo
    )

    /*
    Validar existencia
    */

    if (!usuario || !amigo) {

      return res.status(404).send({
        error: 'Usuario no encontrado'
      })

    }

    /*
    Agregar amigo al usuario
    */

    if (
      !usuario.amigos.includes(idAmigo)
    ) {

      usuario.amigos.push(idAmigo)

    }

    /*
    Agregar usuario al amigo
    */

    if (
      !amigo.amigos.includes(id)
    ) {

      amigo.amigos.push(id)

    }

    guardarData()

    /*
    Respuesta
    */

    res.send({
      mensaje: 'Amigo agregado'
    })

  }
)

/*
#####################################################
METAS
#####################################################
*/

/*
=====================================
OBTENER METAS VISIBLES
=====================================

GET /metas?userId=1

Solo devuelve metas donde:
- el usuario es dueño
- o pertenece como miembro
*/

app.get('/metas', (req, res) => {

  /*
  Obtener usuario
  */

  const userId =
    parseInt(req.query.userId)

  /*
  Filtrar metas visibles
  */

  const metasVisibles = metas.filter(

    meta => {

      return (

        meta.idPrincipal === userId ||

        meta.miembros.includes(userId)

      )

    }

  )

  /*
  Respuesta
  */

  res.send(metasVisibles)

})

/*
=====================================
CREAR META
=====================================

POST /metas

Body:
{
  "titulo": "Moto",
  "montoObjetivo": 12000000,
  "imagen": "...",
  "idPrincipal": 1
}
*/

app.post('/metas', (req, res) => {

  /*
  Crear meta
  */

  const nuevaMeta = {

    id: metas.length + 1,

    titulo: req.body.titulo,

    montoObjetivo:
      req.body.montoObjetivo,

    imagen: req.body.imagen,

    idPrincipal:
      req.body.idPrincipal,

    /*
    El dueño entra automáticamente
    como miembro
    */

    miembros: [
      req.body.idPrincipal
    ]

  }

  /*
  Guardar meta
  */

  metas.push(nuevaMeta)

  /*
  Respuesta
  */

  guardarData()

  res.status(201).send(
    nuevaMeta
  )

})

/*
=====================================
DETALLE DE META
=====================================

GET /metas/:id?idPrincipal=1

Devuelve:
- datos de la meta
- pagos
- total ahorrado
- porcentaje
*/

app.get('/metas/:id', (req, res) => {

  /*
  Obtener datos
  */

  const metaId =
    parseInt(req.params.id)

  const idUsuario =
    parseInt(req.query.idPrincipal)

  /*
  Buscar meta
  */

  const meta = metas.find(
    m => m.id === metaId
  )

  /*
  Validar existencia
  */

  if (!meta) {

    return res.status(404).send({
      error: 'Meta no encontrada'
    })

  }

  /*
  Validar acceso
  */

  const tieneAcceso =

    meta.idPrincipal ===
      idUsuario ||

    meta.miembros.includes(
      idUsuario
    )

  if (!tieneAcceso) {

    return res.status(403).send({
      error:
        'No tienes acceso a esta meta'
    })

  }

  /*
  Obtener pagos
  */

  const pagosDeLaMeta = pagos.filter(

    p => p.metaId === metaId

  )

  /*
  Calcular total ahorrado
  */

  const totalSalvado =
    pagosDeLaMeta.reduce(

      (sum, pago) =>

        sum + pago.montoAportado,

      0

    )

  /*
  Calcular porcentaje
  */

  const porcentaje =

    (totalSalvado /
      meta.montoObjetivo) * 100

  /*
  Respuesta
  */

  res.send({

    ...meta,

    pagosDeLaMeta,

    totalSalvado,

    porcentaje

  })

})

/*
=====================================
AGREGAR MIEMBRO A META
=====================================

POST /metas/:idMeta/miembros

Body:
{
  "idUsuario": 2,
  "idSolicitante": 1
}

Solo el dueño puede agregar miembros.
Solo puede agregar amigos.
*/

app.post(
  '/metas/:idMeta/miembros',

  (req, res) => {

    /*
    Obtener datos
    */

    const idMeta =
      parseInt(req.params.idMeta)

    const idUsuario =
      req.body.idUsuario

    const idSolicitante =
      req.body.idSolicitante

    /*
    Buscar meta
    */

    const meta = metas.find(
      m => m.id === idMeta
    )

    /*
    Validar existencia
    */

    if (!meta) {

      return res.status(404).send({
        error: 'Meta no encontrada'
      })

    }

    /*
    Solo el dueño puede agregar
    */

    if (
      meta.idPrincipal !==
      idSolicitante
    ) {

      return res.status(403).send({
        error:
          'Solo el dueño puede agregar miembros'
      })

    }

    /*
    Buscar dueño
    */

    const dueño = usuarios.find(

      u =>
        u.id === meta.idPrincipal

    )

    /*
    Validar amistad
    */

    if (
      !dueño.amigos.includes(
        idUsuario
      )
    ) {

      return res.status(400).send({
        error:
          'Solo puedes agregar amigos'
      })

    }

    /*
    Agregar miembro
    */

    if (
      !meta.miembros.includes(
        idUsuario
      )
    ) {

      meta.miembros.push(
        idUsuario
      )
      guardarData()

    }

    /*
    Respuesta
    */

    res.send({
      mensaje: 'Miembro agregado'
    })

  }
)

/*
#####################################################
PAGOS
#####################################################
*/

/*
=====================================
REGISTRAR PAGO
=====================================

POST /pagos

Body:
{
  "metaId": 1,
  "idMiembro": 2,
  "montoAportado": 50000
}
*/

app.post('/pagos', (req, res) => {

  /*
  Buscar meta
  */

  const meta = metas.find(

    m => m.id === req.body.metaId

  )

  /*
  Validar existencia
  */

  if (!meta) {

    return res.status(404).send({
      error: 'Meta no encontrada'
    })

  }

  /*
  Validar membresía
  */

  const esMiembro =

    meta.miembros.includes(
      req.body.idMiembro
    )

  if (!esMiembro) {

    return res.status(403).send({
      error:
        'No perteneces a esta meta'
    })

  }

  /*
  Crear pago
  */

  const nuevoPago = {

    id: pagos.length + 1,

    metaId:
      req.body.metaId,

    idMiembro:
      req.body.idMiembro,

    montoAportado:
      req.body.montoAportado,

    fecha:
      new Date().toLocaleDateString()

  }

  /*
  Guardar pago
  */

  pagos.push(nuevoPago)

  /*
  Respuesta
  */

  guardarData()

  res.status(201).send(
    nuevoPago
  )

})

/*
=====================================
CONSULTAR PAGOS DE UNA META
=====================================

GET /metas/:metaId/pagos
*/

app.get(
  '/metas/:metaId/pagos',

  (req, res) => {

    /*
    Obtener id meta
    */

    const metaId =
      parseInt(req.params.metaId)

    /*
    Filtrar pagos
    */

    const pagosDeLaMeta =
      pagos.filter(

        p => p.metaId === metaId

      )

    /*
    Respuesta
    */

    res.send(pagosDeLaMeta)

  }
)

/*
#####################################################
INICIAR SERVIDOR
#####################################################
*/

app.listen(port, () => {

  console.log(
    `Servidor ejecutándose en puerto ${port}`
  )

})