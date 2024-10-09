import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.page.html',
  styleUrls: ['./edit-user.page.scss'],
})
export class EditUserPage implements OnInit {
  userForm: FormGroup;
  userId: number;

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private router: Router,
    private formBuilder: FormBuilder
  ) {
    this.userId = +this.route.snapshot.paramMap.get('id')!;
    this.userForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: [''] // Campo opcional para actualizar la contraseña
    });
  }

  ngOnInit(): void {
    this.userService.getUserById(this.userId).subscribe(
      (user) => {
        this.userForm.patchValue({
          email: user.email
        });
      },
      (error) => {
        console.error('Error al obtener el usuario', error);
      }
    );
  }

  onSubmit() {
    if (this.userForm.valid) {
      this.userService.updateUser(this.userId, this.userForm.value).subscribe(
        () => {
          this.router.navigate(['/users']);
        },
        (error) => {
          console.error('Error al actualizar usuario', error);
        }
      );
    }
  }
}
