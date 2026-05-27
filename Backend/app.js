import express from 'express'
import fs from 'fs'

import {
  usuarios,
  metas,
  pagos
} from './data.js'


const app = express()
const port = 3000

app.use(express.json()) //Permite recibir JSON desde Retrofit o cualquier cliente HTTP.


/*
====================================================
Funcion para guardar, y actualizar datos en data.js
====================================================
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
==============================================
Servicios para usuarios
==============================================
*/

//------ Obtener todos los usuarios ------
app.get('/usuarios', (req, res) => {

  res.send(usuarios)

})

/*
--------- Buscar usuario por id de dispositivo ---------

Ejemplo:
GET /usuarios/dispositivo/12345

Se usa para identificar al usuario
desde el celular sin necesidad del login.
*/

app.get(
  '/usuarios/dispositivo/:idDispositivo',

  (req, res) => {

    const dispositivoId =
      req.params.idDispositivo
      //miramos que trea el request params
      //lo guardamos en una variable para usarlo despues

    const usuario = usuarios.find( //Usamos .find para buscar, en este caso el usuario
      u => u.idDispositivo === dispositivoId // argumento de busqueda, comparamos el id del dispositivo con el que nos llega por params
    )

    if (!usuario) { // miramos si no encontramos el usuario, si es asi respondemos con un error 404
      return res.status(404).send({
        error: 'Usuario no encontrado'
      })
    }

    res.send(usuario) // si encontramos el usuario respondemos con sus datos

  }
)

//------ Crear nuevo usuario ------ 
// con POST para crear nuevos recursos, en este caso un nuevo usuario

app.post('/usuarios', (req, res) => {

  // creamos un objeto con los datos del nuevo usuario, e id dinamimca
  const nuevoUsuario = {

    id: usuarios.length + 1,

    nombre: req.body.nombre, //obtener el nombre del body del request

    idDispositivo: req.body.idDispositivo, //obtener el id del dispositivo del body del request

    amigos: [] // recien creado no tiene amigos, se inicializa como un array vacio

  }

  usuarios.push(nuevoUsuario) // guardamos de manera temporal en la ram del server

  guardarData() // guardamos en el archivo data.js para persistencia

  res.status(201).send( nuevoUsuario ) // respondemos con el nuevo usuario creado, y un status 201 que indica que se ha creado un recurso

})

//------ Editar a un usuario ------ 
// con PUT para actualizar recursos, en este caso un usuario existente

app.put('/usuarios/:id', (req, res) => {

  const id =
    parseInt(req.params.id) //desde android nos llega el id como string (se lo enviamos al abrir la app, cuando colsulta id por dispositivo),
    //lo convertimos a numero para compararlo con los ids de los usuarios que son numeros

  // ------------------------------------------------------------
  //revisa que exista y no haya inconsistencias con android,
  //puede pasar que se envie un id que no existe, o que se envie un id con formato incorrecto, por eso es importante validar
  const usuario = usuarios.find( 
    u => u.id === id
  )

  if (!usuario) {

    return res.status(404).send({
      error: 'Usuario no encontrado'
    })

  }
  //-------------------------------------------------------------

  usuario.nombre =
    req.body.nombre //actualizamos el nombre del usuario con el valor que nos llega en el body del request desde android

  guardarData() // guardamos en el archivo data.js para persistencia

  res.send(usuario)// reenbiamos el usuario actualizado como respuesta

})

//------ Agregar amigo a usuario ------

app.post(
  '/usuarios/:id/amigos',

  (req, res) => {

    const id =
      parseInt(req.params.id) // obtenemos el id del usuario al que queremos agregar un amigo, 
      // lo convertimos a numero

    const idAmigo =
      req.body.friendId // obtenemos el id del amigo que queremos agregar, Desde el mapeo de android, se lo enviamos en el body del request con la clave friendId, lo obtenemos con req.body.friendId



    const usuario = usuarios.find( // busca que exista el usuario al que queremos agregar un amigo
      u => u.id === id
    )

    const amigo = usuarios.find( // busca que exista el amigo que queremos agregar
      u => u.id === idAmigo
    )

    if (!usuario || !amigo) { // si no existe el usuario o el amigo respondemos con un error 404

      return res.status(404).send({
        error: 'Usuario no encontrado'
      })

    }

    if ( !usuario.amigos.includes(idAmigo) ) { // si el amigo no esta ya en la lista de amigos del usuario, lo agregamos
      usuario.amigos.push(idAmigo)
    }


    if (!amigo.amigos.includes(id) ) {// si el usuario no esta ya en la lista de amigos del amigo, lo agregamos para que la amistad sea reciproca
      amigo.amigos.push(id)
    }

    guardarData()// guardamos en el archivo data.js para persistencia

    res.send({
      mensaje: 'Amigo agregado'// respondemos con un mensaje de exito
    })

  }
)


/*
=====================================
Servicios para metas
=====================================
*/

//------ Obtener metas visibles para un usuario ------

app.get('/metas', (req, res) => {

  const userId = parseInt(req.query.userId) // obtenemos el id del usuario desde los query params, lo convertimos a numero

  const metasVisibles = metas.filter( // filtramos las metas para obtener solo las que son visibles para el usuario, es decir, 
  // las que el usuario es dueño o miembro

    meta => {
      return (
        meta.idPrincipal === userId || meta.miembros.includes(userId)
      )
    }
  )

  res.send(metasVisibles) // respondemos con las metas visibles para el usuario
})

//------ Crear nueva meta ------

app.post('/metas', (req, res) => {

/// Crear nueva meta con datos del body del request, e id dinamico
  const nuevaMeta = {
    id: metas.length + 1,
    titulo: req.body.titulo,
    montoObjetivo:req.body.montoObjetivo,
    imagen: req.body.imagen,
    idPrincipal:req.body.idPrincipal,
    miembros: [ req.body.idPrincipal ] // el creador de la meta es el primer miembro
  }

  metas.push(nuevaMeta) // guardamos de manera temporal en la ram del server

  guardarData() // guardamos en el archivo data.js para persistencia

  res.status(201).send(
    nuevaMeta // respondemos con la nueva meta creada, y un status 201 que indica que se ha creado un recurso
  )

})

//------ Obtener detalles de una meta ------

app.get('/metas/:id', (req, res) => {

  const metaId = parseInt(req.params.id) // obtenemos el id de la meta desde los params, lo convertimos a numero

  const idUsuario = parseInt(req.query.idPrincipal) // obtenemos el id del usuario desde los query params, lo convertimos a numero

  const meta = metas.find(
    m => m.id === metaId // buscamos la meta por id
  )

  if (!meta) { // si no existe la meta respondemos con un error 404
    return res.status(404).send({
      error: 'Meta no encontrada'
    })
  }

  // Validar acceso: solo el dueño o miembros pueden acceder a los detalles de la meta
  const tieneAcceso =
    meta.idPrincipal === idUsuario || meta.miembros.includes(idUsuario)

    // si el usuario no es el dueño ni miembro, respondemos con un error 403
  if (!tieneAcceso) {
    return res.status(403).send({
      error:
        'No tienes acceso a esta meta'
    })
  }

  // Filtrar pagos de esta meta
  const pagosDeLaMeta = pagos.filter(
    p => p.metaId === metaId // filtramos los pagos para obtener solo los que pertenecen a esta meta
  )

  // Calcular total salvado para esta meta
  const totalSalvado =
    pagosDeLaMeta.reduce(
      (sum, pago) => // sumamos el monto aportado de cada pago para obtener el total salvado
        sum + pago.montoAportado, 0 // el 0 es el valor inicial de la suma
    )
  
    // Calcular porcentaje de avance
  const porcentaje =

    (totalSalvado /
      meta.montoObjetivo) * 100


  res.send({// respondemos con los detalles de la meta, incluyendo los pagos, total salvado y porcentaje de avance
    ...meta,
    pagosDeLaMeta,
    totalSalvado,
    porcentaje
  })

})

// ----- Agregar miembro a una meta ------
app.post( '/metas/:idMeta/miembros',(req, res) => {

    const idMeta = parseInt(req.params.idMeta) // obtenemos el id de la meta desde los params, lo convertimos a numero

    const idUsuario = req.body.idUsuario // obtenemos el id del usuario que queremos agregar desde el body del request, lo convertimos a numero

    const idSolicitante = req.body.idSolicitante // obtenemos el id del usuario que hace la solicitud desde el body del request, lo convertimos a numero

    
    const meta = metas.find( // buscamos la meta por id
      m => m.id === idMeta
    )

    if (!meta) { // si no existe la meta respondemos con un error 404
      return res.status(404).send({
        error: 'Meta no encontrada'
      })
    }

    if ( meta.idPrincipal !== idSolicitante) { // si el solicitante no es el dueño de la meta, respondemos con un error 403
      return res.status(403).send({
        error:
          'Solo el dueño puede agregar miembros'
      })
    }

    const dueño = usuarios.find( // buscamos el usuario dueño de la meta
      u => u.id === meta.idPrincipal
    )

    if (!dueño.amigos.includes(idUsuario)) {// si el usuario que se quiere agregar no es amigo del dueño, respondemos con un error 403
      return res.status(400).send({
        error:
          'Solo puedes agregar amigos'
      })
    }

    if (!meta.miembros.includes( idUsuario)) {
    // si el usuario no es ya miembro de la meta, lo agregamos
      meta.miembros.push( idUsuario)
      guardarData() // guardamos en el archivo data.js para persistencia si se pudo agregar el miembro

    }

    res.send({
      mensaje: 'Miembro agregado'
    })

  }
)

/*
=====================================
Servicios para pagos
=====================================
*/


//------ Realizar o crear un pago a una meta ------
app.post('/pagos', (req, res) => {

  const meta = metas.find(// buscamos la meta a la que se quiere hacer el pago, para validar que exista y que el usuario sea miembro
    m => m.id === req.body.metaId
  )

  if (!meta) { // si no existe la meta respondemos con un error 404
    return res.status(404).send({
      error: 'Meta no encontrada'
    })
  }

  const esMiembro = meta.miembros.includes
    (
      req.body.idMiembro
    )// validamos que el usuario que hace el pago sea miembro de la meta

  if (!esMiembro) {// si el usuario no es miembro de la meta, respondemos con un error 403
    return res.status(403).send({
      error:
        'No perteneces a esta meta'
    })
  }

  const nuevoPago = {// creamos un nuevo pago con los datos del body del request, e id dinamico
    id: pagos.length + 1,
    metaId: req.body.metaId,
    idMiembro: req.body.idMiembro,
    montoAportado: req.body.montoAportado,
    fecha: new Date().toLocaleDateString()
  }

  pagos.push(nuevoPago)// guardamos de manera temporal en la ram del server

  guardarData() // guardamos en el archivo data.js para persistencia

  res.status(201).send(
    nuevoPago// respondemos con el nuevo pago creado, y un status 201 que indica que se ha creado un recurso
  )

})

//------ Obtener pagos de una meta ------

app.get('/metas/:metaId/pagos', (req, res) => {

    const metaId = parseInt(req.params.metaId) // obtenemos el id de la meta desde los params, lo convertimos a numero

    const pagosDeLaMeta = // filtramos los pagos para obtener solo los que pertenecen a esta meta
      pagos.filter(
        p => p.metaId === metaId
      )
    res.send(pagosDeLaMeta) // respondemos con los pagos de la meta
  }
)

/*
=====================================
INICIAR SERVIDOR
=====================================
*/
app.listen(port, () => {
  console.log(
    `Servidor ejecutándose en puerto ${port}`
  )
})