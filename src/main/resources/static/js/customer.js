// Customer Interface JavaScript - Pure JavaScript (No jQuery)
document.addEventListener("DOMContentLoaded", () => {
  // Initialize components
  initializeDateInputs()
  initializeFormValidation()
  initializeImageGallery()

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
})

// Date Input Initialization
function initializeDateInputs() {
  const today = new Date().toISOString().split("T")[0]
  const tomorrow = new Date(Date.now() + 24 * 60 * 60 * 1000).toISOString().split("T")[0]

  const pickupDateInputs = document.querySelectorAll('input[name="pickupDate"]')
  const returnDateInputs = document.querySelectorAll('input[name="returnDate"]')

  pickupDateInputs.forEach((input) => {
    input.min = today
    if (!input.value) {
      input.value = today
    }
  })

  returnDateInputs.forEach((input) => {
    input.min = tomorrow
    if (!input.value) {
      input.value = tomorrow
    }
  })

  // Update return date when pickup date changes
  pickupDateInputs.forEach((pickupInput) => {
    pickupInput.addEventListener("change", function () {
      const pickupDate = new Date(this.value)
      const minReturnDate = new Date(pickupDate.getTime() + 24 * 60 * 60 * 1000)
      const returnInput = document.querySelector('input[name="returnDate"]')
      if (returnInput) {
        returnInput.min = minReturnDate.toISOString().split("T")[0]
        if (new Date(returnInput.value) <= pickupDate) {
          returnInput.value = minReturnDate.toISOString().split("T")[0]
        }
      }
    })
  })
}

// Form Validation
function initializeFormValidation() {
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

  // Password confirmation validation
  const confirmPasswordInputs = document.querySelectorAll(
    'input[name="confirmPassword"], input[name="confirmNewPassword"]',
  )
  confirmPasswordInputs.forEach((confirmInput) => {
    confirmInput.addEventListener("input", function () {
      const passwordInput = document.querySelector('input[name="password"], input[name="newPassword"]')
      if (passwordInput && this.value !== passwordInput.value) {
        this.setCustomValidity("Mật khẩu xác nhận không khớp")
      } else {
        this.setCustomValidity("")
      }
    })
  })
}

// Image Gallery
function initializeImageGallery() {
  const thumbnails = document.querySelectorAll(".thumbnail-images img")
  const mainImage = document.querySelector(".main-image img")

  thumbnails.forEach((thumbnail) => {
    thumbnail.addEventListener("click", function () {
      if (mainImage) {
        mainImage.src = this.src.replace("100x150", "400x600")
      }
    })
  })
}

// Utility Functions
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

function togglePassword(inputId) {
  const input = document.getElementById(inputId)
  const button = input.nextElementSibling
  const icon = button.querySelector("i")

  if (input.type === "password") {
    input.type = "text"
    icon.classList.remove("fa-eye")
    icon.classList.add("fa-eye-slash")
  } else {
    input.type = "password"
    icon.classList.remove("fa-eye-slash")
    icon.classList.add("fa-eye")
  }
}

// Car Functions
function addToWishlist(carId) {
  // Simulate API call
  showAlert("Đã thêm vào danh sách yêu thích!", "success")
}

function shareCard(carId) {
  if (navigator.share) {
    navigator
      .share({
        title: "Xe cho thuê",
        text: "Xem xe này trên FU Car Rental",
        url: window.location.origin + "/cars/" + carId,
      })
      .catch((error) => {
        console.log("Error sharing:", error)
      })
  } else {
    // Fallback: copy to clipboard
    const url = window.location.origin + "/cars/" + carId
    if (navigator.clipboard) {
      navigator.clipboard
        .writeText(url)
        .then(() => {
          showAlert("Đã sao chép link!", "success")
        })
        .catch(() => {
          showAlert("Không thể sao chép link!", "danger")
        })
    } else {
      showAlert("Đã sao chép link!", "success")
    }
  }
}

function bookNow(carId) {
  const pickupDate = document.querySelector('input[name="pickupDate"]')
  const returnDate = document.querySelector('input[name="returnDate"]')

  const pickupValue = pickupDate ? pickupDate.value : ""
  const returnValue = returnDate ? returnDate.value : ""

  if (!pickupValue || !returnValue) {
    showAlert("Vui lòng chọn ngày nhận và trả xe!", "warning")
    return
  }

  const url = "/cars/" + carId + "?pickupDate=" + pickupValue + "&returnDate=" + returnValue
  window.location.href = url
}

// Alert Function
function showAlert(message, type) {
  type = type || "info"

  const alertDiv = document.createElement("div")
  alertDiv.className = "alert alert-" + type + " alert-dismissible fade show position-fixed"
  alertDiv.style.top = "100px"
  alertDiv.style.right = "20px"
  alertDiv.style.zIndex = "9999"
  alertDiv.style.minWidth = "300px"
  alertDiv.innerHTML =
    message + '<button type="button" class="btn-close" onclick="this.parentElement.remove()"></button>'

  document.body.appendChild(alertDiv)

  // Auto remove after 5 seconds
  setTimeout(() => {
    if (alertDiv.parentElement) {
      alertDiv.remove()
    }
  }, 5000)
}

// Search and Filter Functions
function debounce(func, wait) {
  let timeout
  return function executedFunction() {
    const args = arguments
    const later = function () {
      clearTimeout(timeout)
      func.apply(this, args)
    }
    clearTimeout(timeout)
    timeout = setTimeout(later, wait)
  }
}

// Smooth scrolling for anchor links
document.addEventListener("click", (e) => {
  if (e.target.matches('a[href^="#"]')) {
    e.preventDefault()
    const target = document.querySelector(e.target.getAttribute("href"))
    if (target) {
      target.scrollIntoView({
        behavior: "smooth",
        block: "start",
      })
    }
  }
})

// Form submission handling
function handleFormSubmit(formId, successMessage) {
  const form = document.getElementById(formId)
  if (form) {
    form.addEventListener("submit", (e) => {
      e.preventDefault()

      // Simulate form submission
      setTimeout(() => {
        showAlert(successMessage || "Thao tác thành công!", "success")
      }, 1000)
    })
  }
}

// Initialize specific page functions
function initializeHomePage() {
  // Home page specific initialization
  const searchForm = document.getElementById("searchForm")
  if (searchForm) {
    handleFormSubmit("searchForm", "Đang tìm kiếm xe phù hợp...")
  }
}

function initializeCarsPage() {
  // Cars page specific initialization
  const filterForm = document.getElementById("filterForm")
  if (filterForm) {
    handleFormSubmit("filterForm", "Đang lọc kết quả...")
  }
}

// Call page-specific initialization based on current page
if (window.location.pathname === "/" || window.location.pathname === "/home") {
  initializeHomePage()
} else if (window.location.pathname === "/cars") {
  initializeCarsPage()
}
