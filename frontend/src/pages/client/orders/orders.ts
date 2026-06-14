import { getPedidosPorUsuarioApi } from "../../../utils/api";
import { getSession, logout } from "../../../utils/auth";

const session = getSession();

if (!session) {
  window.location.replace("/src/pages/auth/login/login.html");
}

const ESTADO_LABELS: Record<string, string> = {
  PENDIENTE: "⏳ Pendiente",
  CONFIRMADO: "✅ Confirmado",
  TERMINADO: "🏁 Terminado",
  CANCELADO: "❌ Cancelado",
};

function showModal(pedido: any): void {
  const modal = document.getElementById("order-modal");
  const content = document.getElementById("order-detail-content");
  if (!modal || !content) return;

  content.innerHTML = "";

  const estado = document.createElement("p");
  estado.innerHTML = `<strong>Estado:</strong> ${ESTADO_LABELS[pedido.estado] ?? pedido.estado}`;

  const fecha = document.createElement("p");
  fecha.innerHTML = `<strong>Fecha:</strong> ${pedido.fecha}`;

  const pago = document.createElement("p");
  pago.innerHTML = `<strong>Método de pago:</strong> ${pedido.formaPago}`;

  const titulo = document.createElement("h3");
  titulo.textContent = "Productos:";

  const lista = document.createElement("ul");
  lista.className = "order-detail-list";

  pedido.detalles?.forEach((detalle: any) => {
    const li = document.createElement("li");
    li.textContent = `${detalle.producto?.nombre ?? "Producto"} x${detalle.cantidad} — $${detalle.subtotal?.toLocaleString("es-AR")}`;
    lista.appendChild(li);
  });

  const total = document.createElement("p");
  total.className = "order-total";
  total.innerHTML = `<strong>Total:</strong> $${pedido.total?.toLocaleString("es-AR")}`;

  content.append(estado, fecha, pago, titulo, lista, total);
  modal.style.display = "flex";
}

function hideModal(): void {
  const modal = document.getElementById("order-modal");
  if (modal) modal.style.display = "none";
}

function renderOrders(pedidos: any[]): void {
  const listEl = document.getElementById("orders-list");
  const emptyMsg = document.getElementById("empty-msg");
  if (!listEl || !emptyMsg) return;

  if (pedidos.length === 0) {
    emptyMsg.style.display = "block";
    return;
  }

  emptyMsg.style.display = "none";

  pedidos.forEach((pedido) => {
    const card = document.createElement("div");
    card.className = "order-card";

    const header = document.createElement("div");
    header.className = "order-card-header";

    const id = document.createElement("span");
    id.textContent = `Pedido #${pedido.id}`;

    const estadoBadge = document.createElement("span");
    estadoBadge.className = `badge-estado badge-${pedido.estado.toLowerCase()}`;
    estadoBadge.textContent = ESTADO_LABELS[pedido.estado] ?? pedido.estado;

    header.append(id, estadoBadge);

    const fecha = document.createElement("p");
    fecha.textContent = `Fecha: ${pedido.fecha}`;

    const productos = document.createElement("p");
    const primeros = pedido.detalles?.slice(0, 3) ?? [];
    const nombresProductos = primeros
      .map((d: any) => d.producto?.nombre ?? "Producto")
      .join(", ");
    productos.textContent = `Productos: ${nombresProductos}${pedido.detalles?.length > 3 ? ` +${pedido.detalles.length - 3} más` : ""}`;

    const total = document.createElement("p");
    total.className = "order-total";
    total.textContent = `Total: $${pedido.total?.toLocaleString("es-AR")}`;

    const btnDetalle = document.createElement("button");
    btnDetalle.className = "btn-secondary";
    btnDetalle.textContent = "Ver detalle";
    btnDetalle.addEventListener("click", () => showModal(pedido));

    card.append(header, fecha, productos, total, btnDetalle);
    listEl.appendChild(card);
  });
}

async function init(): Promise<void> {
  if (!session) return;

  try {
    const pedidos = await getPedidosPorUsuarioApi(session.id);
    renderOrders(pedidos);
  } catch (error) {
    console.error("Error al cargar pedidos:", error);
  }

  document.getElementById("logout-btn")?.addEventListener("click", () => logout());
  document.getElementById("btn-close-modal")?.addEventListener("click", () => hideModal());
}

init();