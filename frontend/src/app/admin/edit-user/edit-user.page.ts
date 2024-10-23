import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { ToastController } from '@ionic/angular';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.page.html',
  styleUrls: ['./edit-user.page.scss'],
})
export class EditUserPage implements OnInit {
  userId: number;
  userForm: FormGroup;
  submitted = false;
  selectedRoles: string[] = [];
  roles = [
    { id: 'admin', name: 'Administrador' },
    { id: 'mod', name: 'Moderador' },
    { id: 'user', name: 'Usuario' },
  ];

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private userService: UserService,
    private authService: AuthService,
    private toastController: ToastController,
    private router: Router
  ) {
    this.userForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      university: [''],
      phone: [''],
      dni: ['', Validators.required],
      password: [''],
    });

    const id = this.route.snapshot.paramMap.get('id');
    this.userId = id ? +id : 0;
  }

  ngOnInit() {

    console.log("es admin: ", this.hasAdminRole())
    // Si no es admin y no es el propietario de los datos
    if (!this.hasAdminRole() && this.authService.getLoggedInUserId() !== this.userId){
      this.router.navigate(['/unauthorized']);
    } else {
      this.loadUserData();
    }

  }

  hasAdminRole(): boolean {
    return this.authService.hasRole('ROLE_ADMIN'); 
  }

  async loadUserData() {
    try {
      const userData = await this.userService.getUserById(this.userId).toPromise();
      this.userForm.patchValue({
        firstName: userData.firstName,
        lastName: userData.lastName,
        email: userData.email,
        university: userData.university,
        phone: userData.phone,
        dni: userData.dni,
        password: '',
      });
  
      // Inicializa los roles seleccionados
      this.selectedRoles = userData.roles.map((role: string) => {
        // Asegúrate de que solo obtienes 'mod' en lugar de 'moderador'
        if (role === 'ROLE_MODERATOR') {
          return 'mod';
        }
        return role.replace('ROLE_', '').toLowerCase();
      });
    } catch (error) {
      this.presentToast('Error al cargar los datos del usuario.');
    }
  }
  

  async saveUser() {
    this.submitted = true;
  
    if (this.userForm.valid) {
      const userData = {
        ...this.userForm.value,
        roles: [...new Set(this.selectedRoles)], // Elimina duplicados
      };
  
      // Solo agrega la contraseña si se ha modificado
      if (!this.userForm.value.password) {
        delete userData.password;
      }
  
      console.log('Roles a enviar:', userData.roles); // Verifica aquí los roles
  
      try {
        await this.userService.updateUser(this.userId, userData).toPromise();
        this.presentToast('Usuario actualizado exitosamente.');
      } catch (error) {
        this.presentToast('Error al actualizar el usuario.');
      }
    }
  }
  

  onRoleChange(event: any, roleId: string) {
    // Verifica si el checkbox está marcado
    if (event.detail.checked) {
      // Solo agregar si no está ya en la lista
      if (!this.selectedRoles.includes(roleId)) {
        // Si se selecciona 'mod', se añade 'mod' en lugar de 'moderador'
        if (roleId === 'mod') {
          this.selectedRoles.push(roleId);
        } else if (roleId === 'admin' || roleId === 'user') {
          this.selectedRoles.push(roleId);
        }
      }
    } else {
      // Eliminar el rol si el checkbox se desmarca
      this.selectedRoles = this.selectedRoles.filter(role => role !== roleId);
    }
  }
  
  

  async presentToast(message: string) {
    const toast = await this.toastController.create({
      message,
      duration: 2000,
      position: 'top'
    });
    toast.present();
  }
}
