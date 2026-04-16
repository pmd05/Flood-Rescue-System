const API_BASE_URL = 'http://localhost:8080/api';

function getToken() {
  return localStorage.getItem('token') || '';
}

function getRole() {
  return localStorage.getItem('role') || '';
}

function getHeaders(withJson = true) {
  const headers = {};
  if (withJson) headers['Content-Type'] = 'application/json';
  if (getToken()) headers['Authorization'] = 'Bearer ' + getToken();
  return headers;
}

async function apiFetch(path, options = {}) {
  const response = await fetch(API_BASE_URL + path, {
    ...options,
    headers: {
      ...getHeaders(options.body ? true : false),
      ...(options.headers || {})
    }
  });

  const text = await response.text();
  let data = {};
  try {
    data = text ? JSON.parse(text) : {};
  } catch (e) {
    data = { success: response.ok, message: text || 'Có lỗi xảy ra' };
  }

  if (response.status === 401) {
    alert(data.message || 'Phiên đăng nhập hết hạn hoặc chưa đăng nhập');
    logout();
    throw new Error(data.message || '401 Unauthorized');
  }

  if (response.status === 403) {
    throw new Error(data.message || '403 Forbidden');
  }

  if (!response.ok || data.success === false) {
    throw new Error(data.message || 'Có lỗi xảy ra');
  }

  return data;
}

function logout() {
  localStorage.clear();
  window.location.href = 'login.html';
}

function redirectByRole(role) {
  if (role === 'citizen') window.location.href = 'citizen.html';
  if (role === 'rescue_team') window.location.href = 'team.html';
  if (role === 'coordinator') window.location.href = 'coordinator.html';
  if (role === 'manager') window.location.href = 'manager.html';
  if (role === 'admin') window.location.href = 'admin.html';
}

function requireRole(allowedRoles) {
  const role = getRole();
  const token = getToken();

  if (!token) {
    alert('Bạn cần đăng nhập để truy cập trang này');
    window.location.href = 'login.html';
    return;
  }

  if (!allowedRoles.includes(role)) {
    alert('Bạn không có quyền truy cập trang này');
    redirectByRole(role);
  }
}
