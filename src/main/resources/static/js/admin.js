// Admin Dashboard JavaScript
document.addEventListener("DOMContentLoaded", () => {
  // Sidebar toggle functionality
  const sidebarCollapse = document.getElementById("sidebarCollapse")
  const sidebarCollapseTop = document.getElementById("sidebarCollapseTop")
  const sidebar = document.getElementById("sidebar")
  const overlay = document.querySelector(".overlay")

  if (sidebarCollapse) {
    sidebarCollapse.addEventListener("click", () => {
      sidebar.classList.toggle("active")
      overlay.classList.toggle("active")
    })
  }

  if (sidebarCollapseTop) {
    sidebarCollapseTop.addEventListener("click", () => {
      sidebar.classList.add("active")
      overlay.classList.add("active")
    })
  }

  if (overlay) {
    overlay.addEventListener("click", () => {
      sidebar.classList.remove("active")
      overlay.classList.remove("active")
    })
  }

  // Auto-hide alerts
  const alerts = document.querySelectorAll(".alert")
  alerts.forEach((alert) => {
    setTimeout(() => {
      alert.style.opacity = "0"
      setTimeout(() => {
        alert.remove()
      }, 300)
    }, 5000)
  })

  // Form validation
  const forms = document.querySelectorAll(".needs-validation")
  forms.forEach((form) => {
    form.addEventListener("submit", (event) => {
      if (!form.checkValidity()) {
        event.preventDefault()
        event.stopPropagation()
      }
      form.classList.add("was-validated")
    })
  })

  // DataTable initialization (if using DataTables)
  if (typeof $.fn.DataTable !== "undefined") {
    $(".data-table").DataTable({
      responsive: true,
      language: {
        url: "//cdn.datatables.net/plug-ins/1.10.24/i18n/Vietnamese.json",
      },
    })
  }

  // Tooltip initialization
  const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
  tooltipTriggerList.map((tooltipTriggerEl) => new bootstrap.Tooltip(tooltipTriggerEl))

  // Popover initialization
  const popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'))
  popoverTriggerList.map((popoverTriggerEl) => new bootstrap.Popover(popoverTriggerEl))
})

// Customer Management Functions
function viewCustomer(customerId) {
  // Implement view customer functionality
  window.location.href = `/admin/customers/${customerId}`
}

function editCustomer(customerId) {
  // Implement edit customer functionality
  window.location.href = `/admin/customers/${customerId}/edit`
}

function deleteCustomer(customerId) {
  if (confirm("Bạn có chắc chắn muốn xóa khách hàng này?")) {
    // Send delete request
    fetch(`/admin/customers/${customerId}/delete`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "X-Requested-With": "XMLHttpRequest",
      },
    })
      .then((response) => response.json())
      .then((data) => {
        if (data.success) {
          showAlert("Xóa khách hàng thành công!", "success")
          setTimeout(() => {
            location.reload()
          }, 1000)
        } else {
          showAlert("Có lỗi xảy ra khi xóa khách hàng!", "danger")
        }
      })
      .catch((error) => {
        console.error("Error:", error)
        showAlert("Có lỗi xảy ra khi xóa khách hàng!", "danger")
      })
  }
}

// Car Management Functions
function viewCar(carId) {
  window.location.href = `/admin/cars/${carId}`
}

function editCar(carId) {
  window.location.href = `/admin/cars/${carId}/edit`
}

function deleteCar(carId) {
  if (confirm("Bạn có chắc chắn muốn xóa xe này? Lưu ý: Xe đang được thuê sẽ chỉ được cập nhật trạng thái.")) {
    fetch(`/admin/cars/${carId}/delete`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "X-Requested-With": "XMLHttpRequest",
      },
    })
      .then((response) => response.json())
      .then((data) => {
        if (data.success) {
          showAlert(data.message, "success")
          setTimeout(() => {
            location.reload()
          }, 1000)
        } else {
          showAlert(data.message || "Có lỗi xảy ra khi xóa xe!", "danger")
        }
      })
      .catch((error) => {
        console.error("Error:", error)
        showAlert("Có lỗi xảy ra khi xóa xe!", "danger")
      })
  }
}

// Rental Management Functions
function viewRental(rentalId) {
  window.location.href = `/admin/rentals/${rentalId}`
}

function editRental(rentalId) {
  window.location.href = `/admin/rentals/${rentalId}/edit`
}

function approveRental(rentalId) {
  if (confirm("Bạn có chắc chắn muốn duyệt giao dịch này?")) {
    fetch(`/admin/rentals/${rentalId}/approve`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "X-Requested-With": "XMLHttpRequest",
      },
    })
      .then((response) => response.json())
      .then((data) => {
        if (data.success) {
          showAlert("Duyệt giao dịch thành công!", "success")
          setTimeout(() => {
            location.reload()
          }, 1000)
        } else {
          showAlert("Có lỗi xảy ra khi duyệt giao dịch!", "danger")
        }
      })
      .catch((error) => {
        console.error("Error:", error)
        showAlert("Có lỗi xảy ra khi duyệt giao dịch!", "danger")
      })
  }
}

function cancelRental(rentalId) {
  if (confirm("Bạn có chắc chắn muốn hủy giao dịch này?")) {
    fetch(`/admin/rentals/${rentalId}/cancel`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "X-Requested-With": "XMLHttpRequest",
      },
    })
      .then((response) => response.json())
      .then((data) => {
        if (data.success) {
          showAlert("Hủy giao dịch thành công!", "success")
          setTimeout(() => {
            location.reload()
          }, 1000)
        } else {
          showAlert("Có lỗi xảy ra khi hủy giao dịch!", "danger")
        }
      })
      .catch((error) => {
        console.error("Error:", error)
        showAlert("Có lỗi xảy ra khi hủy giao dịch!", "danger")
      })
  }
}

// Utility Functions
function showAlert(message, type = "info") {
  const alertDiv = document.createElement("div")
  alertDiv.className = `alert alert-${type} alert-dismissible fade show position-fixed`
  alertDiv.style.top = "20px"
  alertDiv.style.right = "20px"
  alertDiv.style.zIndex = "9999"
  alertDiv.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `

  document.body.appendChild(alertDiv)

  // Auto remove after 5 seconds
  setTimeout(() => {
    alertDiv.remove()
  }, 5000)
}

function formatCurrency(amount) {
  return new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
  }).format(amount)
}

function formatDate(dateString) {
  const date = new Date(dateString)
  return date.toLocaleDateString("vi-VN")
}

// Export functions
function exportToExcel(url) {
  window.open(url + "?format=excel", "_blank")
}

function exportToPDF(url) {
  window.open(url + "?format=pdf", "_blank")
}

// Search functionality
function debounce(func, wait) {
  let timeout
  return function executedFunction(...args) {
    const later = () => {
      clearTimeout(timeout)
      func(...args)
    }
    clearTimeout(timeout)
    timeout = setTimeout(later, wait)
  }
}

// Live search
const searchInputs = document.querySelectorAll('input[name="search"]')
searchInputs.forEach((input) => {
  input.addEventListener(
    "input",
    debounce(function () {
      this.form.submit()
    }, 500),
  )
})
