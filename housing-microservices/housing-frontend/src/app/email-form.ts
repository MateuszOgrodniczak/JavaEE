export class EmailForm {
  constructor(
    public addressee: string,
    public username: string,
    public subject: string,
    public text: string
  ) {  }
}
