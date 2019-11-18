from django import forms
from django.contrib.auth.models import User
from .models import Device, Building


class DeviceUpdateForm(forms.ModelForm):
    class Meta:
        model = Device
        fields = ['image']

#TODO dependent dropdown
class DeviceCreateForm(forms.ModelForm):
    class Meta:
        model = Device
        fields = ['devicename', 'type', 'longitude', 'latitude', 'height','url', 'image']

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.fields['floor'].queryset = Device.objects.none()
        self.fields['room'].queryset = Device.objects.none()

class DeviceCreateFormTest(forms.ModelForm):
    devices = forms.ModelChoiceField(queryset= Building.objects.all())


