# Generated by Django 2.2.6 on 2019-10-17 20:23

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('site_plan', '0003_auto_20191017_1555'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='Device',
            name='latitute',
        ),
        migrations.AddField(
            model_name='Device',
            name='latitude',
            field=models.DecimalField(decimal_places=6, default='0', max_digits=9),
        ),
        migrations.AlterField(
            model_name='Device',
            name='building',
            field=models.CharField(default='Shapiro', max_length=200),
        ),
        migrations.AlterField(
            model_name='Device',
            name='floor',
            field=models.IntegerField(default='0'),
        ),
        migrations.AlterField(
            model_name='Device',
            name='ip',
            field=models.GenericIPAddressField(default='0.0.0.0'),
        ),
        migrations.AlterField(
            model_name='Device',
            name='longitute',
            field=models.DecimalField(decimal_places=6, default='0', max_digits=9),
        ),
        migrations.AlterField(
            model_name='Device',
            name='name',
            field=models.CharField(default='unnamed', max_length=100),
        ),
        migrations.AlterField(
            model_name='Device',
            name='room',
            field=models.CharField(default='0', max_length=20),
        ),
        migrations.AlterField(
            model_name='Device',
            name='type',
            field=models.CharField(default='no type', max_length=100),
        ),
        migrations.AlterField(
            model_name='Device',
            name='url',
            field=models.URLField(default='http://www.ipIoTDevice.com/'),
        ),
    ]
