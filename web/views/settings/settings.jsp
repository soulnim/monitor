<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Settings - Monitor</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="../includes/header.jsp" />
    
    <div class="dashboard-container">
        <jsp:include page="../includes/sidebar.jsp" />
        
        <main class="main-content">
            <div class="page-header">
                <h1>Settings</h1>
                <p class="page-subtitle">Manage your application preferences</p>
            </div>
            
            <c:if test="${param.updated == 'notifications'}">
                <div class="alert alert-success">Notification settings updated successfully!</div>
            </c:if>
            <c:if test="${param.updated == 'preferences'}">
                <div class="alert alert-success">Preferences updated successfully!</div>
            </c:if>
            <c:if test="${param.updated == 'privacy'}">
                <div class="alert alert-success">Privacy settings updated successfully!</div>
            </c:if>
            
            <!-- Settings Tabs -->
            <div class="settings-container">
                
                <!-- Account Settings -->
                <div class="settings-card">
                    <div class="settings-header">
                        <h2>👤 Account Settings</h2>
                    </div>
                    <div class="settings-body">
                        <div class="settings-item">
                            <div class="settings-info">
                                <h4>Profile Information</h4>
                                <p>Update your name, email, and other personal details</p>
                            </div>
                            <div class="settings-action">
                                <a href="${pageContext.request.contextPath}/profile/edit" class="btn btn-secondary">Edit Profile</a>
                            </div>
                        </div>
                        
                        <div class="settings-item">
                            <div class="settings-info">
                                <h4>Password</h4>
                                <p>Change your account password</p>
                            </div>
                            <div class="settings-action">
                                <a href="${pageContext.request.contextPath}/profile/change-password" class="btn btn-secondary">Change Password</a>
                            </div>
                        </div>
                        
                        <div class="settings-item">
                            <div class="settings-info">
                                <h4>Session Timeout</h4>
                                <p>Current: ${sessionTimeout} minutes</p>
                            </div>
                            <div class="settings-action">
                                <span class="badge badge-info">Active</span>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- Notification Settings -->
                <div class="settings-card">
                    <div class="settings-header">
                        <h2>🔔 Notifications</h2>
                    </div>
                    <div class="settings-body">
                        <form method="post" action="${pageContext.request.contextPath}/settings">
                            <input type="hidden" name="action" value="notifications" />
                            
                            <div class="settings-item">
                                <div class="settings-info">
                                    <h4>Email Notifications</h4>
                                    <p>Receive updates and reminders via email</p>
                                </div>
                                <div class="settings-action">
                                    <label class="toggle-switch">
                                        <input type="checkbox" name="emailNotifications" value="true" checked>
                                        <span class="toggle-slider"></span>
                                    </label>
                                </div>
                            </div>
                            
                            <div class="settings-item">
                                <div class="settings-info">
                                    <h4>Bill Reminders</h4>
                                    <p>Get notified about upcoming bills</p>
                                </div>
                                <div class="settings-action">
                                    <label class="toggle-switch">
                                        <input type="checkbox" name="billReminders" value="true" checked>
                                        <span class="toggle-slider"></span>
                                    </label>
                                </div>
                            </div>
                            
                            <div class="settings-item">
                                <div class="settings-info">
                                    <h4>Task Reminders</h4>
                                    <p>Get notified about task due dates</p>
                                </div>
                                <div class="settings-action">
                                    <label class="toggle-switch">
                                        <input type="checkbox" name="taskReminders" value="true" checked>
                                        <span class="toggle-slider"></span>
                                    </label>
                                </div>
                            </div>
                            
                            <div class="settings-footer">
                                <button type="submit" class="btn btn-primary">Save Notification Settings</button>
                            </div>
                        </form>
                    </div>
                </div>
                
                <!-- Appearance & Preferences -->
                <div class="settings-card">
                    <div class="settings-header">
                        <h2>🎨 Preferences</h2>
                    </div>
                    <div class="settings-body">
                        <form method="post" action="${pageContext.request.contextPath}/settings">
                            <input type="hidden" name="action" value="preferences" />
                            
                            <div class="settings-item">
                                <div class="settings-info">
                                    <h4>Theme</h4>
                                    <p>Choose your preferred theme</p>
                                </div>
                                <div class="settings-action">
                                    <select name="theme" class="settings-select">
                                        <option value="light" selected>Light</option>
                                        <option value="dark">Dark (Coming Soon)</option>
                                        <option value="auto">Auto (Coming Soon)</option>
                                    </select>
                                </div>
                            </div>
                            
                            <div class="settings-item">
                                <div class="settings-info">
                                    <h4>Language</h4>
                                    <p>Select your preferred language</p>
                                </div>
                                <div class="settings-action">
                                    <select name="language" class="settings-select">
                                        <option value="en" selected>English</option>
                                        <option value="es">Spanish (Coming Soon)</option>
                                        <option value="fr">French (Coming Soon)</option>
                                    </select>
                                </div>
                            </div>
                            
                            <div class="settings-item">
                                <div class="settings-info">
                                    <h4>Date Format</h4>
                                    <p>How dates should be displayed</p>
                                </div>
                                <div class="settings-action">
                                    <select name="dateFormat" class="settings-select">
                                        <option value="MM/DD/YYYY" selected>MM/DD/YYYY</option>
                                        <option value="DD/MM/YYYY">DD/MM/YYYY</option>
                                        <option value="YYYY-MM-DD">YYYY-MM-DD</option>
                                    </select>
                                </div>
                            </div>
                            
                            <div class="settings-item">
                                <div class="settings-info">
                                    <h4>Currency</h4>
                                    <p>Default currency for financial data</p>
                                </div>
                                <div class="settings-action">
                                    <select name="currency" class="settings-select">
                                        <option value="USD" selected>USD ($)</option>
                                        <option value="EUR">EUR (€)</option>
                                        <option value="GBP">GBP (£)</option>
                                        <option value="JPY">JPY (¥)</option>
                                    </select>
                                </div>
                            </div>
                            
                            <div class="settings-footer">
                                <button type="submit" class="btn btn-primary">Save Preferences</button>
                            </div>
                        </form>
                    </div>
                </div>
                
                <!-- Privacy & Security -->
                <div class="settings-card">
                    <div class="settings-header">
                        <h2>🔒 Privacy & Security</h2>
                    </div>
                    <div class="settings-body">
                        <form method="post" action="${pageContext.request.contextPath}/settings">
                            <input type="hidden" name="action" value="privacy" />
                            
                            <div class="settings-item">
                                <div class="settings-info">
                                    <h4>Profile Visibility</h4>
                                    <p>Control who can see your profile</p>
                                </div>
                                <div class="settings-action">
                                    <select name="profileVisibility" class="settings-select">
                                        <option value="private" selected>Private</option>
                                        <option value="friends">Friends Only</option>
                                        <option value="public">Public</option>
                                    </select>
                                </div>
                            </div>
                            
                            <div class="settings-item">
                                <div class="settings-info">
                                    <h4>Data Sharing</h4>
                                    <p>Allow anonymous usage data collection</p>
                                </div>
                                <div class="settings-action">
                                    <label class="toggle-switch">
                                        <input type="checkbox" name="dataSharing" value="true">
                                        <span class="toggle-slider"></span>
                                    </label>
                                </div>
                            </div>
                            
                            <div class="settings-item">
                                <div class="settings-info">
                                    <h4>Two-Factor Authentication</h4>
                                    <p>Add an extra layer of security</p>
                                </div>
                                <div class="settings-action">
                                    <span class="badge badge-secondary">Coming Soon</span>
                                </div>
                            </div>
                            
                            <div class="settings-footer">
                                <button type="submit" class="btn btn-primary">Save Privacy Settings</button>
                            </div>
                        </form>
                    </div>
                </div>
                
                <!-- Data Management -->
                <div class="settings-card">
                    <div class="settings-header">
                        <h2>💾 Data Management</h2>
                    </div>
                    <div class="settings-body">
                        <div class="settings-item">
                            <div class="settings-info">
                                <h4>Export Data</h4>
                                <p>Download all your data in CSV format</p>
                            </div>
                            <div class="settings-action">
                                <button class="btn btn-secondary" disabled>Export (Coming Soon)</button>
                            </div>
                        </div>
                        
                        <div class="settings-item">
                            <div class="settings-info">
                                <h4>Clear Cache</h4>
                                <p>Clear temporary data and reset preferences</p>
                            </div>
                            <div class="settings-action">
                                <button class="btn btn-secondary" onclick="alert('Cache cleared!')">Clear Cache</button>
                            </div>
                        </div>
                        
                        <div class="settings-item danger-zone">
                            <div class="settings-info">
                                <h4>⚠️ Delete Account</h4>
                                <p>Permanently delete your account and all data</p>
                            </div>
                            <div class="settings-action">
                                <button class="btn btn-danger" onclick="confirmDelete()">Delete Account</button>
                            </div>
                        </div>
                    </div>
                </div>
                
            </div>
        </main>
    </div>
    
    <script>
        function confirmDelete() {
            if (confirm('Are you sure you want to delete your account? This action cannot be undone!')) {
                if (confirm('This will permanently delete all your data. Are you absolutely sure?')) {
                    alert('Account deletion is not yet implemented. Please contact support.');
                }
            }
        }
    </script>
</body>
</html>
