import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import {
  ActionPerformed,
  PushNotificationSchema,
  PushNotifications,
  Token,
} from '@capacitor/push-notifications';

@Injectable({
  providedIn: 'root'
})
export class NotificationsPushService {
  private deviceToken: string | null = null;

  constructor(private router: Router) { }

  init() {
    console.log('Initializing notifications');
    
    PushNotifications.requestPermissions().then(result => {
      if (result.receive === 'granted') {
        PushNotifications.register();
      }
    });
    this.addListener();
  }

  private addListener() {
    PushNotifications.addListener('registration',
      (token: Token) => {
        this.deviceToken = token.value; // Guarda el token
        alert('Push registration success, token: ' + this.deviceToken);
      }
    );

    PushNotifications.addListener('registrationError',
      (error: any) => {
        alert('Error on registration: ' + JSON.stringify(error));
      }
    );

    PushNotifications.addListener('pushNotificationReceived',
      (notification: PushNotificationSchema) => {
        alert('Push received: ' + JSON.stringify(notification));
      }
    );

    PushNotifications.addListener('pushNotificationActionPerformed',
      (notification: ActionPerformed) => {
        const requestType = notification.notification.data.type; // Extraer el tipo de notificación
        const requestId = notification.notification.data.requestId; // Extraer el ID de la solicitud

        console.log('Request Type:', requestType); // Agrega este log

        console.log('Request ID:', requestId); // Agrega este log
    
        if (requestType === 'new_loan_request' && requestId) {
          this.router.navigate(['/loan-request-details', requestId]); // Redirigir solo para nuevas solicitudes
        }
      }
    );
  }

  // Método para obtener el token del dispositivo
  getDeviceToken(): string | null {
    return this.deviceToken; // Retorna el token del dispositivo
  }
}
