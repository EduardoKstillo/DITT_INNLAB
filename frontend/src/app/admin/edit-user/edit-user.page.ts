import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { ToastController } from '@ionic/angular';

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.page.html',
  styleUrls: ['./edit-user.page.scss'],
})
export class EditUserPage implements OnInit {

  userId: number;
  userForm: FormGroup = this.fb.group({});
  userRoles: string[] = ['ROLE_USER', 'ROLE_MODERATOR', 'ROLE_ADMIN']; // Lista de roles disponibles
  selectedRoles: string[] = []; // Roles seleccionados por el usuario
  submitted = false;  // <-- Nueva propiedad para seguimiento de envío de formulario

  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private userService: UserService,
    private router: Router,
    private toastController: ToastController
  ) {
    this.userId = +this.route.snapshot.paramMap.get('id')!;
   }

  ngOnInit() {
    // Inicializar el formulario
    this.userForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      university: ['', Validators.required],
      phone: [''],
      dni: ['', Validators.required],
      roles: [[], Validators.required] // Añadimos que roles sea requerido
    });

    this.loadUserDetails();
  }

  loadUserDetails() {
    this.userService.getUserById(this.userId).subscribe((user) => {
      this.userForm.patchValue({
        firstName: user.firstName,
        lastName: user.lastName,
        email: user.email,
        university: user.university,
        phone: user.phone,
        dni: user.dni,
        roles: user.roles
      });
      this.selectedRoles = user.roles;
    });
  }

  onRoleChange(event: CustomEvent, role: string) {
    if (event.detail.checked) {
      this.selectedRoles.push(role);
    } else {
      this.selectedRoles = this.selectedRoles.filter(r => r !== role);
    }
    
    // Actualizamos el campo roles del formulario cada vez que cambia el rol seleccionado
    this.userForm.get('roles')?.setValue(this.selectedRoles);
    this.userForm.get('roles')?.updateValueAndValidity();
  }

  async saveUser() {
    this.submitted = true; // Marcamos como enviado

    if (this.userForm.valid) {
      const updatedUser = {
        ...this.userForm.value,
        roles: this.selectedRoles
      };

      this.userService.updateUser(this.userId, updatedUser).subscribe(
        async () => {
          const toast = await this.toastController.create({
            message: 'Usuario actualizado con éxito.',
            duration: 2000
          });
          toast.present();
          this.router.navigate(['/users']); // Redirige a la lista de usuarios
        },
        async (error) => {
          const toast = await this.toastController.create({
            message: 'Error al actualizar el usuario.',
            duration: 2000
          });
          toast.present();
        }
      );
    }
  }
}
