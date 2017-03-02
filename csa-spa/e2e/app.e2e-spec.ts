import { CsaSpaPage } from './app.po';

describe('csa-spa App', function() {
  let page: CsaSpaPage;

  beforeEach(() => {
    page = new CsaSpaPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
