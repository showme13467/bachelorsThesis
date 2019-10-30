from django.db import models
from django.utils import timezone
from django.contrib.auth.models import User
from django.urls import reverse

# Create your models here.


class Building(models.Model):
    name = models.CharField(max_length=100, default = 'unnamed')

    def __str__(self):
        return self.name

class Floor(models.Model):
    name = models.CharField(max_length=100, default='unnamed')
    building = models.ForeignKey(Building, on_delete=models.CASCADE)

    def __str__(self):
        return self.name

class Room(models.Model):
    name = models.CharField(max_length=100, default='unnamed')
    floor = models.ForeignKey(Floor,on_delete=models.CASCADE)

    def __str__(self):
        return self.name

class Device(models.Model):
    TYPE_CHOICES = (
        ('Samsung TV', 'Samsung TV'),
        ('Router', 'Router'),
        ('Phillips Hue', 'Phillips Hue'),
    )

    BUILDING_CHOICES = (
        ('Schapiro', 'Schapiro Center for Engineering and Physical Science Research'),
        ('Uris Hall', 'Uris Hall')
    )

    FLOOR_CHOICES = (
        ('3', '3th Floor'),
        ('4', '4th Floor'),
        ('5', '5th Floor'),
        ('6', '6th Floor'),
        ('7', '7th Floor'),
        ('8', '8th Floor'),
        ('9', '9th Floor'),
        ('10', '10th Floor'),
    )
#TODO implement dependent dropdown list
    name = models.CharField(max_length=100, default='unnamed')
    type = models.CharField(max_length=100, default='no type', choices=TYPE_CHOICES)
    building = models.ForeignKey(Building, on_delete=models.SET_NULL, null=True)
    floor = models.ForeignKey(Floor, on_delete=models.SET_NULL, null=True)
    room = models.ForeignKey(Room, on_delete=models.SET_NULL, null=True)
    longitude = models.DecimalField(max_digits=9, decimal_places=6, default='0')
    latitude = models.DecimalField(max_digits=9, decimal_places=6, default='0')
    height = models.CharField(max_length=4, default='0 m', help_text= "Height above floor level")
    url = models.URLField(max_length=200, default='https://www.ipIoTDevice.com/')
    date_added = models.DateTimeField(default=timezone.now)
    author = models.ForeignKey(User, on_delete=models.CASCADE)
    image = models.ImageField(default='default.jpg', upload_to='device_pics')
    
    def __str__(self):
        return f'{self.name} @ {self.author}'
        
    def get_absolute_url(self):
        return reverse('table-detail', kwargs ={'pk': self.pk})