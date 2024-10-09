import { Component } from '@angular/core';
import { Platform } from '@ionic/angular';
import { NotificationsPushService } from './services/notifications-push.service';
import { Capacitor } from '@capacitor/core';

@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  styleUrls: ['app.component.scss'],
})
export class AppComponent {
  constructor(private platform: Platform, private notificationPushService: NotificationsPushService) {
    this.initializeApp();
  }

  initializeApp() {
    this.platform.ready().then(() => {
      // Habilitar el modo oscuro
      document.body.classList.toggle('dark', true);
    });
    if (Capacitor.isNativePlatform()) {
      // Configurar las notificaciones push
      this.notificationPushService.init();
    }
  }
}
