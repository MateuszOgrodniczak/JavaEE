export class OauthToken {
  constructor(
    public access_token: string,
    public token_type: string,
    public expires_in: number,
    public scope: string
  ) {  }
}
