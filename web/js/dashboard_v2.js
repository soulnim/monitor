// Dashboard V2 - Interactive Features with Edit Mode Toggle
let isEditMode = false;
let sortableInstance = null;

document.addEventListener('DOMContentLoaded', function() {
    console.log('Dashboard V2 JavaScript loaded');
    
    // Check if required elements exist
    const customizeBtn = document.getElementById('customizeToggleBtn');
    const panel = document.getElementById('customizationPanel');
    
    if (!customizeBtn) {
        console.error('Customize button not found!');
        return;
    }
    if (!panel) {
        console.error('Customization panel not found!');
        return;
    }
    
    console.log('All required elements found');
    
    // Initialize features
    initializeCharts();
    loadDashboardLayout();
    initializeCheckboxStyling();
    
    // Event Listener for customize toggle
    customizeBtn.addEventListener('click', function() {
        console.log('Customize toggle clicked');
        toggleEditMode();
    });
    
    console.log('Event listeners attached');
});

// Toggle between view mode and edit mode
function toggleEditMode() {
    isEditMode = !isEditMode;
    
    const panel = document.getElementById('customizationPanel');
    const btn = document.getElementById('customizeToggleBtn');
    const btnText = document.getElementById('customizeBtnText');
    const btnIcon = btn.querySelector('.icon');
    
    if (isEditMode) {
        // Enter edit mode
        console.log('Entering edit mode');
        panel.classList.add('active');
        document.body.classList.add('edit-mode');
        btn.classList.add('confirm-mode');
        btnText.textContent = 'Confirm';
        btnIcon.textContent = '✓';
        
        // Enable drag and drop
        enableDragAndDrop();
        
        // Load current settings into checkboxes
        loadCurrentSettings();
    } else {
        // Exit edit mode and save
        console.log('Exiting edit mode and saving');
        panel.classList.remove('active');
        document.body.classList.remove('edit-mode');
        btn.classList.remove('confirm-mode');
        btnText.textContent = 'Customize';
        btnIcon.textContent = '⚙️';
        
        // Disable drag and drop
        disableDragAndDrop();
        
        // Save the customization
        saveCustomization();
    }
}

// Enable drag and drop functionality
function enableDragAndDrop() {
    const widgetsGrid = document.getElementById('widgetsGrid');
    
    if (typeof Sortable !== 'undefined' && !sortableInstance) {
        sortableInstance = new Sortable(widgetsGrid, {
            animation: 150,
            ghostClass: 'dragging',
            handle: '.widget',
            onEnd: function() {
                console.log('Widget reordered');
            }
        });
        console.log('Drag and drop enabled');
    }
}

// Disable drag and drop
function disableDragAndDrop() {
    if (sortableInstance) {
        sortableInstance.destroy();
        sortableInstance = null;
        console.log('Drag and drop disabled');
    }
}

// Initialize checkbox styling
function initializeCheckboxStyling() {
    // This will be called when entering edit mode
}

// Update checkbox visual style
function updateCheckboxStyle(checkbox) {
    const checkboxItem = checkbox.closest('.widget-toggle');
    if (checkbox.checked) {
        checkboxItem.classList.add('checked');
    } else {
        checkboxItem.classList.remove('checked');
    }
}

// Load current settings into checkboxes
function loadCurrentSettings() {
    const visibleWidgets = getVisibleWidgets();
    const checkboxes = document.querySelectorAll('.widget-toggle input[type="checkbox"]');
    
    console.log('Loading current settings. Visible widgets:', visibleWidgets);
    
    checkboxes.forEach(function(checkbox) {
        checkbox.checked = visibleWidgets.includes(checkbox.value);
        updateCheckboxStyle(checkbox);
        
        // Remove old listeners
        checkbox.removeEventListener('change', handleCheckboxChange);
        
        // Add change listener to immediately update visibility
        checkbox.addEventListener('change', handleCheckboxChange);
    });
}

// Handle checkbox change - shows/hides widget immediately
function handleCheckboxChange() {
    updateCheckboxStyle(this);
    // Immediately show/hide widget for preview
    const widget = document.querySelector('[data-widget-id="' + this.value + '"]');
    if (widget) {
        widget.style.display = this.checked ? '' : 'none';
    }
}

// Save customization
function saveCustomization() {
    console.log('Saving customization');
    
    const checkboxes = document.querySelectorAll('.widget-toggle input[type="checkbox"]');
    const selectedWidgets = [];
    
    checkboxes.forEach(function(checkbox) {
        if (checkbox.checked) {
            selectedWidgets.push(checkbox.value);
        }
    });
    
    console.log('Selected widgets:', selectedWidgets);
    
    // Save widget visibility
    localStorage.setItem('dashboardWidgets', JSON.stringify(selectedWidgets));
    
    // Save widget order
    saveDashboardLayout();
    
    // Apply visibility
    updateWidgetVisibility(selectedWidgets);
    
    console.log('Customization saved to localStorage successfully!');
    
    // Show brief confirmation
    showSaveConfirmation();
}

// Show save confirmation
function showSaveConfirmation() {
    const btn = document.getElementById('customizeToggleBtn');
    const textElement = btn.querySelector('#customizeBtnText');
    const originalText = 'Customize';
    
    textElement.textContent = 'Saved!';
    btn.style.background = '#10b981'; // Green
    
    setTimeout(function() {
        textElement.textContent = originalText;
        btn.style.background = ''; // Reset to default
    }, 1500);
}

// Get currently visible widgets
function getVisibleWidgets() {
    const widgets = document.querySelectorAll('.widget');
    const visibleWidgets = [];
    
    widgets.forEach(function(widget) {
        if (widget.style.display !== 'none') {
            visibleWidgets.push(widget.dataset.widgetId);
        }
    });
    
    return visibleWidgets;
}

// Update widget visibility
function updateWidgetVisibility(selectedWidgets) {
    console.log('Updating widget visibility for:', selectedWidgets);
    const widgets = document.querySelectorAll('.widget');
    console.log('Found widgets:', widgets.length);
    
    let hiddenCount = 0;
    let visibleCount = 0;
    
    widgets.forEach(function(widget) {
        const widgetId = widget.dataset.widgetId;
        if (selectedWidgets.includes(widgetId)) {
            widget.style.display = '';
            visibleCount++;
        } else {
            widget.style.display = 'none';
            hiddenCount++;
        }
    });
    
    console.log('Widgets visible:', visibleCount, 'Widgets hidden:', hiddenCount);
}

// Save dashboard layout (widget order)
function saveDashboardLayout() {
    const widgetsGrid = document.getElementById('widgetsGrid');
    const widgets = widgetsGrid.querySelectorAll('.widget');
    const layout = [];
    
    widgets.forEach(function(widget) {
        layout.push(widget.dataset.widgetId);
    });
    
    localStorage.setItem('dashboardLayout', JSON.stringify(layout));
    console.log('Layout saved:', layout);
}

// Load dashboard layout on page load
function loadDashboardLayout() {
    console.log('Loading dashboard layout from localStorage');
    
    // Load visible widgets
    const savedWidgets = localStorage.getItem('dashboardWidgets');
    console.log('Saved widgets from localStorage:', savedWidgets);
    
    if (savedWidgets) {
        try {
            const selectedWidgets = JSON.parse(savedWidgets);
            console.log('Parsed selected widgets:', selectedWidgets);
            updateWidgetVisibility(selectedWidgets);
            console.log('Widget visibility updated');
        } catch (e) {
            console.error('Error parsing saved widgets:', e);
        }
    } else {
        console.log('No saved widgets found, showing all widgets by default');
    }
    
    // Load widget order
    const savedLayout = localStorage.getItem('dashboardLayout');
    console.log('Saved layout from localStorage:', savedLayout);
    
    if (savedLayout) {
        try {
            const layout = JSON.parse(savedLayout);
            const widgetsGrid = document.getElementById('widgetsGrid');
            
            if (!widgetsGrid) {
                console.error('widgetsGrid element not found');
                return;
            }
            
            console.log('Reordering widgets based on saved layout');
            layout.forEach(function(widgetId) {
                const widget = widgetsGrid.querySelector('[data-widget-id="' + widgetId + '"]');
                if (widget) {
                    widgetsGrid.appendChild(widget);
                }
            });
            console.log('Widget order updated');
        } catch (e) {
            console.error('Error parsing saved layout:', e);
        }
    } else {
        console.log('No saved layout found, using default order');
    }
}

// Chart Initialization
function initializeCharts() {
    initializeExpenseChart();
    initializeTrendChart();
}

// Expense Breakdown Pie Chart
function initializeExpenseChart() {
    const canvas = document.getElementById('expenseChart');
    if (!canvas) return;
    
    const ctx = canvas.getContext('2d');
    
    // Sample data - replace with actual data from server
    const data = {
        labels: ['Food & Dining', 'Transport', 'Shopping', 'Bills', 'Entertainment', 'Others'],
        datasets: [{
            data: [850, 420, 680, 920, 350, 280],
            backgroundColor: [
                '#ef4444',
                '#f59e0b',
                '#10b981',
                '#3b82f6',
                '#8b5cf6',
                '#ec4899'
            ],
            borderWidth: 0
        }]
    };
    
    new Chart(ctx, {
        type: 'doughnut',
        data: data,
        options: {
            responsive: true,
            maintainAspectRatio: true,
            plugins: {
                legend: {
                    position: 'bottom',
                    labels: {
                        padding: 15,
                        font: {
                            size: 12,
                            family: "'Segoe UI', sans-serif"
                        },
                        usePointStyle: true,
                        pointStyle: 'circle'
                    }
                },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            const label = context.label || '';
                            const value = context.parsed || 0;
                            const total = context.dataset.data.reduce((a, b) => a + b, 0);
                            const percentage = ((value / total) * 100).toFixed(1);
                            return label + ': $' + value.toFixed(2) + ' (' + percentage + '%)';
                        }
                    }
                }
            },
            cutout: '65%'
        }
    });
}

// Income vs Expenses Trend Line Chart
function initializeTrendChart() {
    const canvas = document.getElementById('trendChart');
    if (!canvas) return;
    
    const ctx = canvas.getContext('2d');
    
    // Sample data - replace with actual data from server
    const data = {
        labels: ['Aug', 'Sep', 'Oct', 'Nov', 'Dec', 'Jan'],
        datasets: [
            {
                label: 'Income',
                data: [3200, 3500, 3300, 3800, 3600, 4000],
                borderColor: '#10b981',
                backgroundColor: 'rgba(16, 185, 129, 0.1)',
                borderWidth: 3,
                tension: 0.4,
                fill: true,
                pointRadius: 4,
                pointHoverRadius: 6,
                pointBackgroundColor: '#10b981',
                pointBorderColor: '#fff',
                pointBorderWidth: 2
            },
            {
                label: 'Expenses',
                data: [2800, 3200, 2900, 3100, 3400, 3300],
                borderColor: '#ef4444',
                backgroundColor: 'rgba(239, 68, 68, 0.1)',
                borderWidth: 3,
                tension: 0.4,
                fill: true,
                pointRadius: 4,
                pointHoverRadius: 6,
                pointBackgroundColor: '#ef4444',
                pointBorderColor: '#fff',
                pointBorderWidth: 2
            }
        ]
    };
    
    new Chart(ctx, {
        type: 'line',
        data: data,
        options: {
            responsive: true,
            maintainAspectRatio: true,
            interaction: {
                mode: 'index',
                intersect: false,
            },
            plugins: {
                legend: {
                    position: 'top',
                    align: 'end',
                    labels: {
                        padding: 15,
                        font: {
                            size: 12,
                            family: "'Segoe UI', sans-serif",
                            weight: '600'
                        },
                        usePointStyle: true,
                        pointStyle: 'circle',
                        boxWidth: 8
                    }
                },
                tooltip: {
                    backgroundColor: 'rgba(0, 0, 0, 0.8)',
                    padding: 12,
                    titleFont: {
                        size: 13,
                        weight: '600'
                    },
                    bodyFont: {
                        size: 13
                    },
                    callbacks: {
                        label: function(context) {
                            return context.dataset.label + ': $' + context.parsed.y.toFixed(2);
                        }
                    }
                }
            },
            scales: {
                x: {
                    grid: {
                        display: false
                    },
                    ticks: {
                        font: {
                            size: 12
                        }
                    }
                },
                y: {
                    beginAtZero: true,
                    grid: {
                        color: '#f3f4f6',
                        drawBorder: false
                    },
                    ticks: {
                        font: {
                            size: 12
                        },
                        callback: function(value) {
                            return '$' + value;
                        }
                    }
                }
            }
        }
    });
}

// Utility function to format currency
function formatCurrency(amount) {
    return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD',
        minimumFractionDigits: 2
    }).format(amount);
}