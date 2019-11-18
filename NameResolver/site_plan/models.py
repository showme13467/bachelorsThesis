from django.db import models
from django.utils import timezone
from django.contrib.auth.models import User
from django.urls import reverse

# Create your models here.

class Device(models.Model):
    TYPE_CHOICES = (
        ('Samsung TV', 'Samsung TV'),
        ('Router', 'Router'),
        ('Phillips Hue', 'Phillips Hue'),
    )

    # TODO implement dependent dropdown list
    devicename = models.CharField(max_length=100, default='unnamed')
    type = models.CharField(max_length=100, default='no type', choices=TYPE_CHOICES)
    longitude = models.DecimalField(max_digits=9, decimal_places=6, default='0')
    latitude = models.DecimalField(max_digits=9, decimal_places=6, default='0')
    height = models.CharField(max_length=4, default='0 m', help_text="Height above floor level")
    url = models.URLField(max_length=200, default='https://www.ipIoTDevice.com/')
    date_added = models.DateTimeField(default=timezone.now)
    author = models.ForeignKey(User, on_delete=models.CASCADE)
    image = models.ImageField(default='default.jpg', upload_to='device_pics')

    def __str__(self):
        return f'{self.name} @ {self.author}'

    def get_absolute_url(self):
        return reverse('table-detail', kwargs={'pk': self.pk})

class Room(models.Model):
    roomname = models.CharField(max_length=100, default='unnamed')

    def __str__(self):
        return self.name

class Floor(models.Model):
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
    floorname = models.ForeignKey('Room', on_delete=models.CASCADE)
    floorstructure = models.CharField(max_length=100, default='unnamed', choices=FLOOR_CHOICES)

    def __str__(self):
        return self.name

class Building(models.Model):
    BUILDING_CHOICES = (
        ('Schapiro', 'Schapiro Center for Engineering and Physical Science Research'),
        ('Uris Hall', 'Uris Hall')
    )

    buildingname = models.CharField(max_length=100, default='unnamed', choices=BUILDING_CHOICES)
    buildingstructure = models.ForeignKey('Floor', on_delete=models.CASCADE)
    def __str__(self):
        return self.name








