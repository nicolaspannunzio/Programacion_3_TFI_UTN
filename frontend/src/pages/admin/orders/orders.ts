import { getPedidosApi, actualizarEstadoPedidoApi } from "../../../utils/api";
import { getSession, logout } from "../../../utils/auth";

const session = getSession();
if (!session || session.rol !== "ADMIN") {
  window.location.replace("/src/pages/auth/login/login.html");
}

let pedidoSeleccionado: any = null;
let todosPedidos: any[] = [];

const ESTADO_LABELS: Record<string, string> = {
  PENDIENTE: "⏳ Pendiente",
  CONFIRMADO: "✅ Confirmado",
  TERMINADO: "🏁 Terminado",
  CANCELADO: "❌ Cancelado",
};

function showModal(pedido: any): void {
  pedidoSeleccionado = pedido;
  const modal = document.getElementById("modal");
  const detail = document.getElementById("order-detail");
  const selectEstado = document.getElementById("nuevo-estado") as HTMLSelectElement;

  if (!detail) return;

  detail.innerHTML = "";

  const cliente = document.createElement("p");
  cliente.innerHTML = `<strong>Cliente:</strong> ${pedido.usuario?.nombre ?? ""} ${pedido.usuario?.apellido ?? ""}`;

  const fecha = document.createElement("p");
  fecha.innerHTML = `<strong>Fecha:</strong> ${pedido.fecha}`;

  const pago = document.createElement("p");
  pago.innerHTML = `<strong>Método de pago:</strong> ${pedido.formaPago}`;

  const estado = document.createElement("p");
  estado.innerHTML = `<strong>Estado actual:</strong> ${ESTADO_LABELS[pedido.estado] ?? pedido.estado}`;

  const titulo = document.createElement("h3");
  titulo.textContent = "Productos:";

  const lista = document.createElement("ul");
  pedido.detalles?.forEach((d: any) => {
    const li = document.createElement("li");
    li.textContent = `${d.producto?.nombre ?? "Producto"} x${d.cantidad} — $${d.subtotal?.toLocaleString("es-AR")}`;
    lista.appendChild(li);
  });

  const total = document.createElement("p");
  total.className = "order-total";
  total.innerHTML = `<strong>Total:</strong> $${pedido.total?.toLocaleString("es-AR")}`;

  detail.append(cliente, fecha, pago, estado, titulo, lista, total);

  if (selectEstado) selectEstado.value = pedido.estado;
  if (modal) modal.style.display = "flex";
}

function hideModal(): void {
  const modal = document.getElementById("modal");
  if (modal) modal.style.display = "none";
  pedidoSeleccionado = null;
}

function renderOrders(pedidos: any[]): void {
  const listEl = document.getElementById("orders-list");
  const emptyMsg = document.getElementById("empty-msg");
  if (!listEl) return;

  listEl.innerHTML = "";

  if (pedidos.length === 0) {
    if (emptyMsg) emptyMsg.style.display = "block";
    return;
  }

  if (emptyMsg) emptyMsg.style.display = "none";

  pedidos.forEach((pedido) => {
    const card = document.createElement("div");
    card.className = "order-card";

    const header = document.createElement("div");
    header.className = "order-card-header";

    const id = document.createElement("span");
    id.textContent = `Pedido #${pedido.id}`;

    const badge = document.createElement("span");
    badge.className = `badge-estado badge-${pedido.estado.toLowerCase()}`;
    badge.textContent = ESTADO_LABELS[pedido.estado] ?? pedido.estado;

    header.append(id, badge);

    const cliente = document.createElement("p");
    cliente.textContent = `Cliente: ${pedido.usuario?.nombre ?? ""} ${pedido.usuario?.apellido ?? ""}`;

    const fecha = document.createElement("p");
    fecha.textContent = `Fecha: ${pedido.fecha}`;

    const total = document.createElement("p");
    total.className = "order-total";
    total.textContent = `Total: $${pedido.total?.toLocaleString("es-AR")}`;

    const btnDetalle = document.createElement("button");
    btnDetalle.className = "btn-secondary";
    btnDetalle.textContent = "Ver detalle";
    btnDetalle.addEventListener("click", () => showModal(pedido));

    card.append(header, cliente, fecha, total, btnDetalle);
    listEl.appendChild(card);
  });
}

async function init(): Promise<void> {
  if (!session) return;

  todosPedidos = await getPedidosApi();
  renderOrders(todosPedidos);

  document.getElementById("filtro-estado")?.addEventListener("change", (e) => {
    const valor = (e.target as HTMLSelectElement).value;
    if (valor === "TODOS") {
      renderOrders(todosPedidos);
    } else {
      renderOrders(todosPedidos.filter((p) => p.estado === valor));
    }
  });

  document.getElementById("btn-cerrar")?.addEventListener("click", () => hideModal());

  document.getElementById("btn-actualizar")?.addEventListener("click", async () => {
    if (!pedidoSeleccionado) return;
    const nuevoEstado = (document.getElementById("nuevo-estado") as HTMLSelectElement).value;
    await actualizarEstadoPedidoApi(pedidoSeleccionado.id, { estado: nuevoEstado });
    hideModal();
    todosPedidos = await getPedidosApi();
    renderOrders(todosPedidos);
  });

  document.getElementById("logout-btn")?.addEventListener("click", () => logout());
}

init();