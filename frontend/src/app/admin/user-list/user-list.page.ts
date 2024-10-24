import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.page.html',
  styleUrls: ['./user-list.page.scss'],
})
export class UserListPage implements OnInit {
  users: any[] = [];

  constructor(private userService: UserService, private router: Router) {}

  ngOnInit(): void {
    this.getUsers();
  }

  ionViewWillEnter() {
    this.getUsers();  // También recargar las invitaciones cuando entras a la página
  }

  getUsers(event?: any) {
    this.userService.getAllUsers().subscribe(
      (data) => {
        this.users = data;
        if (event) {
          event.target.complete(); // Finaliza el refresco
        }
      },
      (error) => {
        console.error('Error al obtener usuarios', error);
        if (event) {
          event.target.complete(); // Finaliza el refresco
        }
      }
    );
  }

  editUser(id: number) {
    this.router.navigate([`/edit-user/${id}`]);
  }

  deleteUser(id: number) {
    if (confirm('¿Estás seguro de que deseas eliminar este usuario?')) {
      this.userService.deleteUser(id).subscribe(
        () => {
          this.getUsers(); // Refrescar la lista
        },
        (error) => {
          console.error('Error al eliminar usuario', error);
        }
      );
    }
  }
}