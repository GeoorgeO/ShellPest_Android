USE [ShellPest]
GO

/****** Object:  Table [dbo].[t_Cosecha]    Script Date: 30/03/2022 12:58:47 p. m. ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[t_Cosecha](
	[Fecha] [datetime] NOT NULL,
	[Id_Bloque] [char](4) NOT NULL,
	[c_codigo_eps] [char](2) NOT NULL,
	[BICO] [varchar](50) NULL,
	[Cajas_Cosecha] [numeric](10, 0) NULL,
	[Cajas_Desecho] [numeric](10, 0) NULL,
	[Cajas_Pepena] [numeric](10, 0) NULL,
	[Cajas_RDiaria] [numeric](10, 0) NULL,
	[Id_Usuario] [varchar](10) NULL,
	[F_Fecha_Crea] [datetime] NULL,
 CONSTRAINT [PK_t_Cosecha] PRIMARY KEY CLUSTERED 
(
	[Fecha] ASC,
	[c_codigo_eps] ASC,
	[Id_Bloque] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[t_Cosecha]  WITH CHECK ADD  CONSTRAINT [FK_t_Cosecha_t_Usuarios] FOREIGN KEY([Id_Usuario])
REFERENCES [dbo].[t_Usuarios] ([Id_Usuario])
GO

ALTER TABLE [dbo].[t_Cosecha] CHECK CONSTRAINT [FK_t_Cosecha_t_Usuarios]
GO


