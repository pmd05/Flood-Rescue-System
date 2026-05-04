function layout(title, contentHtml) {
  return `
    <header>
      <h2>${title}</h2>
      <div class="nav">
        <button onclick="logout()">Logout</button>
      </div>
    </header>
    <main>${contentHtml}</main>
  `;
}

function renderList(containerId, items, mapper) {
  const container = document.getElementById(containerId);
  container.innerHTML = items.map(mapper).join('') || '<div class="item">Không có dữ liệu</div>';
}

function formatJson(value) {
  return JSON.stringify(value, null, 2);
}
