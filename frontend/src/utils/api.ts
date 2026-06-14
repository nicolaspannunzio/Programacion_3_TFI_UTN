const BASE_URL = "http://localhost:8080/api";

// ==================== AUTH ====================

export async function loginApi(mail: string, contraseña: string) {
  const res = await fetch(`${BASE_URL}/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ mail, contraseña }),
  });
  if (!res.ok) throw new Error("Credenciales incorrectas");
  return res.json();
}

export async function registerApi(data: {
  nombre: string;
  apellido: string;
  mail: string;
  celular: string;
  contraseña: string;
}) {
  const res = await fetch(`${BASE_URL}/auth/register`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });
  if (!res.ok) {
    const err = await res.json();
    throw new Error(err.mensaje || "Error al registrarse");
  }
  return res.json();
}

// ==================== CATEGORIAS ====================

export async function getCategoriasApi() {
  const res = await fetch(`${BASE_URL}/categories`);
  if (!res.ok) throw new Error("Error al obtener categorías");
  return res.json();
}

export async function crearCategoriaApi(data: { nombre: string; descripcion: string }) {
  const res = await fetch(`${BASE_URL}/categories`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });
  if (!res.ok) throw new Error("Error al crear categoría");
  return res.json();
}

export async function actualizarCategoriaApi(id: number, data: { nombre: string; descripcion: string }) {
  const res = await fetch(`${BASE_URL}/categories/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });
  if (!res.ok) throw new Error("Error al actualizar categoría");
  return res.json();
}

export async function eliminarCategoriaApi(id: number) {
  const res = await fetch(`${BASE_URL}/categories/${id}`, {
    method: "DELETE",
  });
  if (!res.ok) throw new Error("Error al eliminar categoría");
}

// ==================== PRODUCTOS ====================

export async function getProductosApi() {
  const res = await fetch(`${BASE_URL}/products`);
  if (!res.ok) throw new Error("Error al obtener productos");
  return res.json();
}

export async function getProductoByIdApi(id: number) {
  const res = await fetch(`${BASE_URL}/products/${id}`);
  if (!res.ok) throw new Error("Producto no encontrado");
  return res.json();
}

export async function getProductosPorCategoriaApi(categoriaId: number) {
  const res = await fetch(`${BASE_URL}/products/categoria/${categoriaId}`);
  if (!res.ok) throw new Error("Error al filtrar productos");
  return res.json();
}

export async function crearProductoApi(data: {
  nombre: string;
  precio: number;
  descripcion: string;
  stock: number;
  imagen: string;
  disponible: boolean;
  idCategoria: number;
}) {
  const res = await fetch(`${BASE_URL}/products`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });
  if (!res.ok) throw new Error("Error al crear producto");
  return res.json();
}

export async function actualizarProductoApi(id: number, data: object) {
  const res = await fetch(`${BASE_URL}/products/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });
  if (!res.ok) throw new Error("Error al actualizar producto");
  return res.json();
}

export async function eliminarProductoApi(id: number) {
  const res = await fetch(`${BASE_URL}/products/${id}`, {
    method: "DELETE",
  });
  if (!res.ok) throw new Error("Error al eliminar producto");
}

// ==================== PEDIDOS ====================

export async function getPedidosApi() {
  const res = await fetch(`${BASE_URL}/orders`);
  if (!res.ok) throw new Error("Error al obtener pedidos");
  return res.json();
}

export async function getPedidosPorUsuarioApi(usuarioId: number) {
  const res = await fetch(`${BASE_URL}/orders/usuario/${usuarioId}`);
  if (!res.ok) throw new Error("Error al obtener pedidos del usuario");
  return res.json();
}

export async function crearPedidoApi(data: {
  idUsuario: number;
  estado: string;
  formaPago: string;
  detallePedido: { idProducto: number; cantidad: number }[];
}) {
  const res = await fetch(`${BASE_URL}/orders`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });
  if (!res.ok) {
    const err = await res.json();
    throw new Error(err.mensaje || "Error al crear pedido");
  }
  return res.json();
}

export async function actualizarEstadoPedidoApi(id: number, data: { estado: string }) {
  const res = await fetch(`${BASE_URL}/orders/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });
  if (!res.ok) throw new Error("Error al actualizar estado del pedido");
  return res.json();
}