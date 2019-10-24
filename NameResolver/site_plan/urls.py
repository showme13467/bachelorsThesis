from django.urls import path
from . import views
from .views import DeviceDeleteView, DevicesListView, DevicesDetailView, DeviceCreateView, DeviceUpdateView


urlpatterns = [
    path('', views.home, name='site-plan-home'),
    path('room/', views.room, name='room-plan'),
    path('table/', DevicesListView.as_view(), name='table-home'),
    path('table/<int:pk>/', DevicesDetailView.as_view(), name='table-detail'),
    path('registerNewDevice/', DeviceCreateView.as_view(), name='device-create'),
    path('table/siteplan/', views.home, name='register-at-table-page'), 
    path('table/<int:pk>/update', DeviceUpdateView.as_view(), name='device-update'),
    path('table/<int:pk>/delete', DeviceDeleteView.as_view(), name='device-delete'),
    
]