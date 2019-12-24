# Generated by Django 2.2.6 on 2019-10-17 19:55

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('site_plan', '0002_auto_20191017_1422'),
    ]

    operations = [
        migrations.RenameField(
            model_name='Device',
            old_name='title',
            new_name='building',
        ),
        migrations.RenameField(
            model_name='Device',
            old_name='date_posted',
            new_name='date_added',
        ),
        migrations.RemoveField(
            model_name='Device',
            name='content',
        ),
        migrations.AddField(
            model_name='Device',
            name='floor',
            field=models.IntegerField(default=0),
            preserve_default=False,
        ),
        migrations.AddField(
            model_name='Device',
            name='ip',
            field=models.GenericIPAddressField(default='0.0.0.0'),
            preserve_default=False,
        ),
        migrations.AddField(
            model_name='Device',
            name='latitute',
            field=models.DecimalField(decimal_places=6, default=0, max_digits=9),
            preserve_default=False,
        ),
        migrations.AddField(
            model_name='Device',
            name='longitute',
            field=models.DecimalField(decimal_places=6, default=0, max_digits=9),
            preserve_default=False,
        ),
        migrations.AddField(
            model_name='Device',
            name='name',
            field=models.CharField(default='unnamed', max_length=100),
            preserve_default=False,
        ),
        migrations.AddField(
            model_name='Device',
            name='room',
            field=models.CharField(default=0, max_length=20),
            preserve_default=False,
        ),
        migrations.AddField(
            model_name='Device',
            name='type',
            field=models.CharField(default='no specific type', max_length=100),
            preserve_default=False,
        ),
        migrations.AddField(
            model_name='Device',
            name='url',
            field=models.URLField(default='https://ipOfTheIotDevice.com/'),
            preserve_default=False,
        ),
    ]