from django.shortcuts import render
from django.db.models import Q
from .models import map
from django.contrib.auth.decorators import login_required
from django.views.generic import ListView, DetailView, CreateView, UpdateView, DeleteView
from .forms import DeviceUpdateForm
from django.contrib.auth.mixins import LoginRequiredMixin, UserPassesTestMixin

# Create your views here.
def home(request):
    return render(request,'siteplan/home.html')

@login_required
def room(request):
    return render(request,'siteplan/room.html')  
 
@login_required 
def tableprint(request):
    context ={
        'devices' : map.objects.all()
    }
    return render(request, 'siteplan/table_homepage.html',context)

@login_required
def search(request):
    template = 'siteplan/table_homepage.html'
    query = request.GET.get('q')
    results = map.objects.filter(Q(name__icontains=query) | Q(author__username__icontains=query) | Q(type__icontains=query) | Q(building__icontains=query) | Q(floor__icontains=query) | Q(url__icontains=query) | Q(room__icontains=query))
    context = {
        'devices': results,
    }
    return render(request, template, context)

 
 
class DevicesListView(LoginRequiredMixin, ListView):
    model = map
    template_name = 'siteplan/table_homepage.html'
    context_object_name = 'devices'
    ordering = ['building', 'floor']

    
    
class DevicesDetailView(LoginRequiredMixin, DetailView):
    model = map
    template_name = 'siteplan/table_detail.html'
 

class DeviceCreateView(LoginRequiredMixin, CreateView):
    model = map
    fields = ['name', 'type', 'building', 'floor', 'room', 'longitude', 'latitude', 'height', 'ip','url', 'image']
    template_name = 'siteplan/registerDevice.html'
    
    def form_valid(self, form):
        form.instance.author = self.request.user
        return super().form_valid(form)
 
class DeviceUpdateView(LoginRequiredMixin,UserPassesTestMixin, UpdateView):
    model = map
    fields = ['name', 'type', 'building', 'floor', 'room', 'longitude', 'latitude', 'height', 'ip','url', 'image']
    template_name = 'siteplan/registerDevice.html'
    
    def form_valid(self, form):
        form.instance.author = self.request.user
        return super().form_valid(form)
    
    def test_func(self):
        device = self.get_object()
        if self.request.user == device.author:
            return True
        return False
        
class DeviceDeleteView(LoginRequiredMixin, UserPassesTestMixin, DeleteView):
    model = map
    template_name = 'siteplan/device_confirm_delete.html'
    success_url = '/'
    
    def test_func(self):
        device = self.get_object()
        if self.request.user == device.author:
            return True
        return False
        
