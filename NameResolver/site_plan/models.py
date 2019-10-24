from django.db import models
from django.utils import timezone
from django.contrib.auth.models import User
from django.urls import reverse

# Create your models here.
class map(models.Model):
    name = models.CharField(max_length=100, default = 'unnamed' )
    type = models.CharField(max_length=100, default = 'no type')
    building = models.CharField(max_length=200, default = 'Shapiro')
    floor = models.CharField(max_length = 2,default = '0')
    room = models.CharField(max_length=20, default = '0')
    longitude = models.DecimalField(max_digits=9, decimal_places=6, default= '0')
    latitude = models.DecimalField(max_digits=9, decimal_places=6, default = '0')
    height = models.CharField(max_length=4, default='0 m')
    ip = models.GenericIPAddressField(protocol='both', unpack_ipv4=False, default = '0.0.0.0')
    url = models.URLField(max_length=200, default = 'https://www.ipIoTDevice.com/')
    date_added = models.DateTimeField(default=timezone.now)
    author = models.ForeignKey(User, on_delete=models.CASCADE)
    image = models.ImageField(default='default.jpg', upload_to='device_pics')
    
    def __str__(self):
        return f'{self.name} @ {self.author}'
        
    def get_absolute_url(self):
        return reverse('table-detail', kwargs ={'pk': self.pk})
